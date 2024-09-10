package com.blautech.service;

import com.blautech.bean.ResponseWS;
import com.blautech.mongodb.model.History;
import com.blautech.mongodb.model.Product;
import com.blautech.mongodb.repository.HistoryRepository;
import com.blautech.mongodb.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class ProductManager {
    private final ProductRepository productRepository;
    private final HistoryRepository historyRepository;

    public ProductManager(ProductRepository productRepository, HistoryRepository historyRepository) {
        this.productRepository = productRepository;
        this.historyRepository = historyRepository;
    }

    public ResponseWS createProduct(Product product) {
        try{
            Optional<Product> existingSku = productRepository.findBySku(product.getSku());

            if(existingSku.isPresent()){
                return new ResponseWS(false, "Product with SKU already exists: " + product.getSku(), null);
            }

            product.setSku(product.getSku());
            product.setStatus(true);
            product.setDetail(product.getDetail());
            product.setCreationDate(LocalDateTime.now());
            product.setModificationDate(LocalDateTime.now());
            productRepository.save(product);

            saveHistory(product, "CREATE");

            return new ResponseWS(true, "Product created successfully", product);
        }catch (RuntimeException e){
            return new ResponseWS(false, "Error while creating document", null);
        }
    }

    public ResponseWS getAllProducts(){
        try {
            List<Product> response = productRepository.findAll()
                    .stream()
                    .filter(Product::isStatus)
                    .toList();

            return new ResponseWS(true, "Products obtained successfully", response);
        }catch (RuntimeException e){
            return new ResponseWS(false, "Error while obtain products", null);
        }
    }

    public ResponseWS getProductBy(String id){
        Optional<Product> response = productRepository.findById(id);
        return new ResponseWS(true, "Product obtained successfully", response);
    }

    public ResponseWS updateProduct(String id, Product product){
        try {
            Optional<Product> existingProduct = productRepository.findById(id);

            if(existingProduct.isEmpty()){
                return new ResponseWS(false, "Product not found with id: " + id, null);
            }

            Product updates = existingProduct.get();
            updates.setSku(product.getSku());
            updates.setStatus(true);
            updates.setModificationDate(LocalDateTime.now());
            updates.setDetail(product.getDetail());
            productRepository.save(updates);

            saveHistory(product, "UPDATE");

            return new ResponseWS(true, "Product updated successfully", product);
        }catch (RuntimeException e) {
            return new ResponseWS(false, "Error while obtain products", null);
        }
    }

    public ResponseWS deleteProduct(String id) {
        try {
            Optional<Product> existingProduct = productRepository.findById(id);

            if (existingProduct.isEmpty()) {
                return new ResponseWS(false, "Product not found with id: " + id, null);
            }

            Product updates = existingProduct.get();
            updates.setStatus(false);
            updates.setModificationDate(LocalDateTime.now());
            productRepository.save(updates);

            saveHistory(updates, "DELETE");

            return new ResponseWS(true, "Product deleted successfully", null);
        } catch (RuntimeException e) {
            return new ResponseWS(false, "Error while deleting product", null);
        }
    }

    private void saveHistory(Product product, String eventType) {
        History history = new History();
        history.setProductId(product.getId());
        history.setEventType(eventType);
        history.setSnapshot(Map.of(
                "sku", product.getSku(),
                "status", product.isStatus(),
                "detail", product.getDetail()
        ));
        history.setDate(LocalDateTime.now());
        historyRepository.save(history);
    }
}
