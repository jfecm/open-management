package com.jfecm.openmanagement.product.util;

import com.jfecm.openmanagement.product.dtos.ProductRequest;
import com.jfecm.openmanagement.product.dtos.ProductResponse;
import com.jfecm.openmanagement.product.model.Product;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductModelMapper {
    private final ModelMapper modelMapper;

    public ProductModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public Product toEntity(ProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }

    public ProductResponse toResponse(Product product) {
        return modelMapper.map(product, ProductResponse.class);
    }

    public void toUpdate(ProductRequest product, Product toUpdate) {
        modelMapper.map(product, toUpdate);
    }
}
