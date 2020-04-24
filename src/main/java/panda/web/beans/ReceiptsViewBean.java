package panda.web.beans;

import org.modelmapper.ModelMapper;
import panda.domain.models.view.ReceiptViewModel;
import panda.service.ReceiptService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class ReceiptsViewBean {
    private List<ReceiptViewModel> receipts;

    private ModelMapper modelMapper;
    private ReceiptService receiptService;

    public ReceiptsViewBean() {
    }
    @Inject
    public ReceiptsViewBean(ModelMapper modelMapper, ReceiptService receiptService) {
        this.modelMapper = modelMapper;
        this.receiptService = receiptService;
        this.initPackages();
    }

    private void initPackages() {
        this.receipts = this.receiptService.findAllReceipts()
                .stream()
                .map(p -> {
                    ReceiptViewModel receiptViewModel = this.modelMapper.map(p, ReceiptViewModel.class);
                    receiptViewModel.setRecipient(p.getRecipient().getUsername());

                    return receiptViewModel;
                })
                .collect(Collectors.toList());
    }

    public List<ReceiptViewModel> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<ReceiptViewModel> receipts) {
        this.receipts = receipts;
    }
}
