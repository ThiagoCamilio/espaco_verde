package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.MPOrderResponseDTO;
import br.com.espaco_verde.DTO.UpdateOrderDTO;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ServiceMercadoPagoWebhook {

    @Value("${mp.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private ServicePayment servicePayment;

    @Autowired
    private ServiceOrder serviceOrder;

    @Async
    public void validateWebhook(String dataId, String type, String xSignature, String xRequestId) {

        if (!isValidSign(dataId, xRequestId, xSignature)) {
            System.err.println("ALERTA DE SEGURANÇA: Assinatura do Webhook inválida para o ID: " + dataId);
            throw new SecurityException("Assinatura inválida ou requisição forjada.");
        }

        MPOrderResponseDTO orderData = servicePayment.fetchOrderDetails(dataId);

        if(orderData != null){
            int orderId = orderData.externalReference();

            if("approved".equals(orderData.status()) && "accredited".equals(orderData.statusDetail())){
                serviceOrder.updateOrderStatus(orderId, new UpdateOrderDTO("Pago"));
            } else if ("canceled".equals(orderData.status()) || "expired".equals(orderData.statusDetail())) {
                serviceOrder.updateOrderStatus(orderId, new UpdateOrderDTO("Cancelado"));
            }
        }

    }

    private boolean isValidSign(String dataId, String xRequestId, String xSignature) {

        try{
            String ts = "";
            String hash = "";

            String[] parts = xSignature.split(",");
            for (String part : parts ){
                String[] keyValue = part.split("=");
                if (keyValue.length == 2){
                    if("ts".equals(keyValue[0].trim())){
                        ts = keyValue[1].trim();
                    } else if ("v1".equals(keyValue[0].trim())) {
                        hash = keyValue[1].trim();
                    }
                }
            }
            String manifest = String.format("id:%s;request-id:%s;ts:%s;", dataId, xRequestId, ts);
            String cyphedSignature = new HmacUtils("HmacSHA256", webhookSecret).hmacHex(manifest);

            return cyphedSignature.equals(hash);
        } catch (Exception e) {
            return false;
        }

    }

}
