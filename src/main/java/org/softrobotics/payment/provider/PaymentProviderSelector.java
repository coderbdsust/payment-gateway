package org.softrobotics.payment.provider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.softrobotics.dto.PaymentDTO;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PaymentProviderSelector {

    @Inject
    Instance<PaymentProvider> paymentProviderInstances;

    @Produces
    public List<PaymentProvider> getAllPaymentProviders() {
        List<PaymentProvider> providers = new ArrayList<>();
        paymentProviderInstances.forEach(providers::add);
        return providers;
    }

    public PaymentProvider selectProvider(PaymentDTO.PaymentRequest request) {
        return paymentProviderInstances.stream()
                .filter(p -> p.supports(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No provider found"));
    }
}
