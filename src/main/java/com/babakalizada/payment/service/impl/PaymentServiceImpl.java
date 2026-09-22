package com.babakalizada.payment.service.impl;

import com.babakalizada.cart.entity.Cart;
import com.babakalizada.cart.repository.ICartRepository;
import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.*;
import com.babakalizada.common.util.TransactionUtils;
import com.babakalizada.order.enums.OrderStatus;
import com.babakalizada.order.entity.Order;
import com.babakalizada.order.entity.OrderItem;
import com.babakalizada.order.repository.IOrderRepository;
import com.babakalizada.payment.enums.PaymentMethod;
import com.babakalizada.payment.enums.PaymentStatus;
import com.babakalizada.payment.dto.request.DtoPaymentRequest;
import com.babakalizada.payment.dto.response.DtoPaymentResponse;
import com.babakalizada.payment.entity.Payment;
import com.babakalizada.payment.repository.IPaymentRepository;
import com.babakalizada.payment.service.IPaymentService;
import com.babakalizada.product.entity.Product;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
import com.babakalizada.wallet.entity.Wallet;
import com.babakalizada.wallet.repository.IWalletRepository;
import com.babakalizada.wallet.service.IWalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentServiceImpl implements IPaymentService {

    private final IPaymentRepository paymentRepository;
    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;
    private final ICartRepository cartRepository;
    private final IWalletRepository walletRepository;
    private final IWalletService walletService;

    @Transactional
    @Override
    public DtoPaymentResponse pay(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Order Not Found!"
                                )
                        )
                );

        User user = getCurrentUser();

        if (!user.getId().equals(order.getUser().getId())) {
            throw new ForbiddenException(
                    new ErrorMessage(
                            ErrorCode.FORBIDDEN,
                            "User cannot access this order"
                    )
            );
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(
                    new ErrorMessage(
                            ErrorCode.BUSINESS_ERROR,
                            "Order status must be PENDING"
                    )
            );
        }

        if (paymentRepository.existsByOrder_Id(order.getId())) {
            throw new PaymentAlreadyExistsException(
                    new ErrorMessage(
                            ErrorCode.PAYMENT_ALREADY_EXISTS,
                            "Order id: " + order.getId()
                    )
            );
        }

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Wallet not found!"
                                )
                        )
                );

        if (wallet.getBalance().compareTo(order.getTotalAmount()) < 0) {
            throw new InsufficientBalanceException(
                    new ErrorMessage(
                            ErrorCode.INSUFFICIENT_BALANCE,
                            "Required amount: " + order.getTotalAmount()
                    )
            );
        }

        List<OrderItem> orderItems = order.getItems();

        for (OrderItem orderItem : orderItems) {

            Product product = orderItem.getProduct();

            if (product.getStock() < orderItem.getQuantity()) {
                throw new BusinessException(
                        new ErrorMessage(
                                ErrorCode.STOCK_NOT_ENOUGH,
                                "Stock Not Enough!"
                        )
                );
            }
        }

        String transactionId = "LOCAL-TOP-UP-" + TransactionUtils.transactionalId();

        walletService.debit(
                wallet.getId(),
                order.getTotalAmount(),
                transactionId
        );

        for (OrderItem orderItem : orderItems) {

            Product product = orderItem.getProduct();

            product.setStock(
                    product.getStock() - orderItem.getQuantity()
            );
        }

        Payment payment = new Payment();

        payment.setUser(user);
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentMethod(PaymentMethod.WALLET);
        payment.setTransactionId(transactionId);

        paymentRepository.save(payment);

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Cart not found!"
                                )
                        )
                );

        cart.getItems().clear();

        DtoPaymentResponse paymentResponse = new DtoPaymentResponse();

        paymentResponse.setOrderId(order.getId());
        paymentResponse.setPaymentId(payment.getId());
        paymentResponse.setPaymentStatus(payment.getPaymentStatus());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setPaymentMethod(payment.getPaymentMethod());
        paymentResponse.setTransactionId(payment.getTransactionId());
        paymentResponse.setCreatedAt(payment.getCreatedAt());

        return paymentResponse;
    }

    @Override
    public DtoPaymentResponse getPaymentByOrderId(DtoPaymentRequest dtoPaymentRequest) {
        Payment payment = paymentRepository
                .findByOrder_Id(dtoPaymentRequest.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                new ErrorMessage(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Payment resource not found!"
                                )
                        )
                );

        User user = getCurrentUser();

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(
                    new ErrorMessage(
                            ErrorCode.FORBIDDEN,
                            "You cannot access this payment"
                    )
            );
        }
        DtoPaymentResponse paymentResponse = new DtoPaymentResponse();
        paymentResponse.setOrderId(dtoPaymentRequest.getOrderId());
        paymentResponse.setPaymentId(payment.getId());
        paymentResponse.setPaymentStatus(payment.getPaymentStatus());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setPaymentMethod(payment.getPaymentMethod());
        paymentResponse.setTransactionId(payment.getTransactionId());
        paymentResponse.setCreatedAt(payment.getCreatedAt());
        return paymentResponse;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findUsersByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorMessage(
                                ErrorCode.USER_NOT_FOUND,
                                "User not found"
                        )));

        return user;
    }
}
