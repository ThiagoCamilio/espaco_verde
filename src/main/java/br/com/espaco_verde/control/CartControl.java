package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.CartDTO;
import br.com.espaco_verde.DTO.ProductCartDTO;
import br.com.espaco_verde.entity.Cart;
import br.com.espaco_verde.entity.ProductCart;
import br.com.espaco_verde.entity.User;
import br.com.espaco_verde.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user/cart")
public class CartControl {

    @Autowired
    private CartService cartService;

    @GetMapping()
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal User logedUser){
        CartDTO cartDTO = new CartDTO(cartService.getCart(logedUser.getId()));
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addProduto(@AuthenticationPrincipal User logedUser, @RequestBody ProductCartDTO productCartDTO){
        CartDTO cartDTO = cartService.addProduct(logedUser.getId(), productCartDTO);
        return ResponseEntity.ok(cartDTO);

    }

    @DeleteMapping("/remove/{productCartId}")
    public ResponseEntity<CartDTO> removeProduct(@AuthenticationPrincipal User logedUser, @PathVariable int productCartId){
        CartDTO cartDTO = cartService.removeProduct(logedUser.getId(), productCartId);
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User logedUser){
        CartDTO cartDTO = cartService.clearCart(logedUser.getId());
        return ResponseEntity.noContent().build();
    }

}
