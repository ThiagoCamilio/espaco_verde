package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.CartDTO;
import br.com.espaco_verde.entity.Cart;
import br.com.espaco_verde.entity.Product;
import br.com.espaco_verde.entity.ProductCart;
import br.com.espaco_verde.entity.User;
import br.com.espaco_verde.repository.CartRepository;
import br.com.espaco_verde.repository.RepositoryProduto;
import br.com.espaco_verde.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private List<ProductCart> produtos = new ArrayList<ProductCart>();

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private RepositoryProduto produtoRepository;

    @Transactional
    public Cart getCart(int userId){

        return cartRepository.findByUserId(userId).orElseGet(() ->{
            User user = repositoryUser.findById(userId).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

    }


    @Transactional
    public CartDTO addProduct(int userId, Integer productId, int quantity){

        Cart cart = getCart(userId);
        Product product = produtoRepository.findById(productId).orElseThrow(()
                -> new RuntimeException("Produto não encontrado"));

        Optional<ProductCart> hasItem = cart.getProductCarts().stream()
                .filter(productCart -> productCart.getProduct().getId() == productId)
                .findFirst();

        if(hasItem.isPresent()){
            ProductCart productCart = hasItem.get();
            productCart.setQuantity(productCart.getQuantity() + quantity);
        }else{
            ProductCart productCart = new ProductCart();
            productCart.setCart(cart);
            productCart.setProduct(product);
            productCart.setQuantity(quantity);
            productCart.setSellPrice(product.getPreco());
            cart.getProductCarts().add(productCart);
        }
        cart.setPrice(cart.getTotal());

        return new CartDTO(cartRepository.save(cart));

    }

    @Transactional
    public CartDTO removeProduct(int userId, int productCartId){
        Cart cart = getCart(userId);
        ProductCart productToRemove = cart.getProductCarts().stream()
                .filter(productCart -> productCart.getId() == productCartId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Produto não encontrado no carrinho"));

        productToRemove.setQuantity(productToRemove.getQuantity() - 1);
        if(productToRemove.getQuantity() <= 0){
            cart.getProductCarts().remove(productToRemove);
        }

        cart.setPrice(cart.getTotal());
        return new CartDTO(cartRepository.save(cart));

    }

    @Transactional
    public CartDTO clearCart(int userId){
        Cart cart = getCart(userId);
        cart.getProductCarts().clear();
        cart.setPrice(cart.getTotal());
        return new CartDTO(cartRepository.save(cart));
    }

}