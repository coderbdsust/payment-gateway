package org.softrobotics.payment.provider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.softrobotics.dto.PaymentDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        if(request.getPaymentSource()!=null && !request.getPaymentSource().isEmpty()){
            return paymentProviderInstances.stream()
                    .filter(p -> p.supportPaymentSource(request))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Payment Source Not Valid"));
        }

        return paymentProviderInstances.stream()
                .filter(p -> p.supportCountry(request) || p.supportIndustry(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No provider found"));
    }
}
