package com.jfecm.openmanagement.product;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductConvert {
    private final ModelMapper modelMapper;

    public ProductConvert(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public Product toEntity(ProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }
    public ProductResponse ToResponse(Product product) {
        return modelMapper.map(product, ProductResponse.class);
    }

    public ProductRequest ToRequest(Product product) {
        return modelMapper.map(product, ProductRequest.class);
    }

    public void toUpdate(ProductRequest product, Product toUpdate) {
        modelMapper.map(product, toUpdate);
    }
}
