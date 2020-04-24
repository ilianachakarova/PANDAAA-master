package panda.service;

import panda.domain.entities.Receipt;
import panda.domain.models.service.ReceiptServiceModel;

import java.util.List;

public interface ReceiptService {
    List<ReceiptServiceModel>findAllReceipts();
    Receipt saveReceipt(ReceiptServiceModel receiptServiceModel);
}
