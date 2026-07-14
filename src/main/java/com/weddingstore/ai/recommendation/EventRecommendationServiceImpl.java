package com.weddingstore.ai.recommendation;

import com.weddingstore.ai.dto.AiProductRecommendationResponse;
import com.weddingstore.ai.dto.AiServiceRecommendationResponse;
import com.weddingstore.ai.dto.EventRecommendationRequest;
import com.weddingstore.ai.prompt.PromptBuilder;
import com.weddingstore.ai.service.AiService;
import com.weddingstore.common.exception.ResourceNotFoundException;
import com.weddingstore.marketplace.product.entity.Product;
import com.weddingstore.marketplace.product.mapper.ProductMapper;
import com.weddingstore.marketplace.product.repository.ProductRepository;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.mapper.ServiceOfferingMapper;
import com.weddingstore.marketplace.service.repository.ServiceOfferingRepository;
import com.weddingstore.planning.event.entity.Event;
import com.weddingstore.planning.event.repository.EventRepository;
import com.weddingstore.user.entity.User;
import com.weddingstore.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventRecommendationServiceImpl
        implements EventRecommendationService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ServiceOfferingRepository serviceRepository;
    private final ProductMapper productMapper;
    private final ServiceOfferingMapper serviceMapper;
    private final AiService aiService;

    @Override
    public AiProductRecommendationResponse recommendProducts(
            String userEmail,
            Long eventId,
            EventRecommendationRequest request
    ) {
        Event event = findOwnedEvent(userEmail, eventId);

        List<Product> products;

        if (request.getCategoryId() != null) {
            products =
                    productRepository
                            .findTop10ByCategoryIdAndActiveTrueOrderByFeaturedDescIdDesc(
                                    request.getCategoryId()
                            );
        } else {
            products =
                    productRepository
                            .findTop10ByActiveTrueOrderByFeaturedDescIdDesc();
        }

        products = filterProductsByPrice(
                products,
                request.getMaxPrice()
        );

        String prompt = PromptBuilder.productRecommendations(
                event,
                request,
                products
        );

        String result = aiService.generateFromPrompt(prompt);

        return AiProductRecommendationResponse.builder()
                .eventId(event.getId())
                .recommendation(result)
                .products(
                        products.stream()
                                .map(productMapper::toResponse)
                                .toList()
                )
                .build();
    }

    @Override
    public AiServiceRecommendationResponse recommendServices(
            String userEmail,
            Long eventId,
            EventRecommendationRequest request
    ) {
        Event event = findOwnedEvent(userEmail, eventId);

        List<ServiceOffering> services;

        if (request.getCategoryId() != null) {
            services =
                    serviceRepository
                            .findTop10ByCategoryIdAndActiveTrueOrderByFeaturedDescRatingDesc(
                                    request.getCategoryId()
                            );
        } else if (event.getCity() != null
                && !event.getCity().isBlank()) {
            services =
                    serviceRepository
                            .findTop10ByCityIgnoreCaseAndActiveTrueOrderByFeaturedDescRatingDesc(
                                    event.getCity()
                            );
        } else {
            services =
                    serviceRepository
                            .findTop10ByActiveTrueOrderByFeaturedDescRatingDesc();
        }

        services = filterServices(
                services,
                event,
                request.getMaxPrice()
        );

        String prompt = PromptBuilder.serviceRecommendations(
                event,
                request,
                services
        );

        String result = aiService.generateFromPrompt(prompt);

        return AiServiceRecommendationResponse.builder()
                .eventId(event.getId())
                .recommendation(result)
                .services(
                        services.stream()
                                .map(serviceMapper::toResponse)
                                .toList()
                )
                .build();
    }

    private Event findOwnedEvent(
            String userEmail,
            Long eventId
    ) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return eventRepository
                .findByIdAndUserIdAndActiveTrue(
                        eventId,
                        user.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found or access denied"
                        )
                );
    }

    private List<Product> filterProductsByPrice(
            List<Product> products,
            BigDecimal maxPrice
    ) {
        if (maxPrice == null) {
            return products;
        }

        return products.stream()
                .filter(product ->
                        effectiveProductPrice(product)
                                .compareTo(maxPrice) <= 0
                )
                .toList();
    }

    private BigDecimal effectiveProductPrice(Product product) {
        return product.getDiscountPrice() != null
                ? product.getDiscountPrice()
                : product.getPrice();
    }

    private List<ServiceOffering> filterServices(
            List<ServiceOffering> services,
            Event event,
            BigDecimal maxPrice
    ) {
        return services.stream()
                .filter(service ->
                        maxPrice == null
                                || service.getBasePrice()
                                .compareTo(maxPrice) <= 0
                )
                .filter(service ->
                        service.getApplicableEventTypes() == null
                                || service.getApplicableEventTypes()
                                .isEmpty()
                                || service.getApplicableEventTypes()
                                .contains(event.getEventType())
                )
                .toList();
    }
}