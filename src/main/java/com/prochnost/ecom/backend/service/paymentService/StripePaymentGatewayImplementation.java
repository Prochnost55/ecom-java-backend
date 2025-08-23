package com.prochnost.ecom.backend.service.paymentService;

import com.prochnost.ecom.backend.dto.paymentDto.PaymentRequestDTO;
import com.prochnost.ecom.backend.dto.paymentDto.PaymentResponseDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StripePaymentGatewayImplementation implements PaymentService{
    
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    
    @Value("${stripe.success.url}")
    private String successUrl;
    
    @Value("${stripe.cancel.url}")
    private String cancelUrl;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }
    
    @Override
    public PaymentResponseDTO makePayment(PaymentRequestDTO paymentRequestDTO) throws StripeException {
        // Create Price object
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setCurrency(paymentRequestDTO.getCurrency())
                .setUnitAmount(paymentRequestDTO.getAmount())
                .setProductData(
                    PriceCreateParams.ProductData.builder()
                        .setName(paymentRequestDTO.getOrderId())
                        .setDescription("Payment for Order: " + paymentRequestDTO.getOrderId())
                        .build()
                )
                .build();

        Price price = Price.create(priceParams);

        // Create Payment Link
        PaymentLinkCreateParams linkParams = PaymentLinkCreateParams.builder()
                .addLineItem(
                    PaymentLinkCreateParams.LineItem.builder()
                        .setPrice(price.getId())
                        .setQuantity(1L)
                        .build()
                )
                .setAfterCompletion(
                    PaymentLinkCreateParams.AfterCompletion.builder()
                        .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                        .setRedirect(
                            PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                .setUrl(successUrl)
                                .build()
                        )
                        .build()
                )
                .build();

        PaymentLink paymentLink = PaymentLink.create(linkParams);

        return PaymentResponseDTO.builder()
                .paymentLinkId(paymentLink.getId())
                .paymentLinkUrl(paymentLink.getUrl())
                .status("CREATED")
                .orderId(paymentRequestDTO.getOrderId())
                .amount(paymentRequestDTO.getAmount())
                .currency(paymentRequestDTO.getCurrency())
                .build();
    }
}