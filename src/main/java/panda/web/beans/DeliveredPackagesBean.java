package panda.web.beans;

import org.modelmapper.ModelMapper;
import panda.domain.entities.Package;
import panda.domain.entities.Status;
import panda.domain.entities.User;
import panda.domain.models.service.PackageServiceModel;
import panda.domain.models.service.ReceiptServiceModel;
import panda.domain.models.view.PackageViewModel;
import panda.domain.models.view.ReceiptViewModel;
import panda.service.PackageService;
import panda.service.ReceiptService;
import panda.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class DeliveredPackagesBean {
    private PackageViewModel packageViewModel;
    private List<PackageViewModel> deliveredPackages;
    private ModelMapper modelMapper;
    private PackageService packageService;
    private UserService userService;
    private ReceiptService receiptService;

    public DeliveredPackagesBean() {
    }
    @Inject
    public DeliveredPackagesBean(ModelMapper modelMapper, PackageService packageService, ReceiptService receiptService, UserService userService) {
        this.modelMapper = modelMapper;
        this.packageService = packageService;
        this.receiptService = receiptService;
        this.userService = userService;
        this.initDeliveredPackages();
        this.packageViewModel = new PackageViewModel();

    }



    private void initDeliveredPackages() {
        this.deliveredPackages = this.packageService.findAllPackagesByStatus(Status.Delivered)
                .stream().map(p->{
                    PackageViewModel packageViewModel = this.modelMapper.map(p,PackageViewModel.class);
                    packageViewModel.setEstimatedDeliveryTime(p.getEstimatedDeliveryTime().toString());
                    packageViewModel.setRecipient(p.getRecipient().getUsername());
                    return packageViewModel;
                }).collect(Collectors.toList());
    }

    public List<PackageViewModel> getDeliveredPackages() {
        return deliveredPackages;
    }

    public void setDeliveredPackages(List<PackageViewModel> deliveredPackages) {
        this.deliveredPackages = deliveredPackages;
    }

    public void showDetails(String id) throws IOException {
        PackageServiceModel packageServiceModel = this.packageService.findPackageById(id);
         packageViewModel = this.modelMapper.map(packageServiceModel, PackageViewModel.class);
         packageViewModel.setRecipient(packageServiceModel.getRecipient().getUsername());

         FacesContext.getCurrentInstance().getExternalContext().redirect("/faces/view/details.xhtml");
    }

    public void changeStatus(String id) throws IOException {
        this.packageService.packageStatusChange(id);

        FacesContext.getCurrentInstance().getExternalContext().redirect("/faces/view/home.xhtml");
    }

    public void changeStatusUser(String id) throws IOException {
        this.packageService.packageStatusChange(id);
        this.generateReceipt(id);

        FacesContext.getCurrentInstance().getExternalContext().redirect("/faces/view/my-receipts.xhtml");
    }

    private void generateReceipt(String id) throws IOException {
        ReceiptViewModel receiptViewModel = new ReceiptViewModel();
        Random random = new Random();
        BigDecimal fee = BigDecimal.valueOf(random.nextInt(150));
        receiptViewModel.setFee(fee);
        receiptViewModel.setIssuedOn(LocalDateTime.now().toString());
        receiptViewModel.setRecipient(this.packageService.findPackageById(id).getRecipient().getUsername());


        ReceiptServiceModel receiptServiceModel = this.modelMapper.map(receiptViewModel, ReceiptServiceModel.class);
        receiptServiceModel.setIssuedOn(LocalDateTime.now());
        User user = this.modelMapper.map(this.userService.findUserByUsername(receiptViewModel.getRecipient()), User.class);
        receiptServiceModel.setRecipient(user);
        receiptServiceModel.setaPackage(this.modelMapper.map(this.packageService.findPackageById(id), Package.class));
        this.receiptService.saveReceipt(receiptServiceModel);


    }

    public PackageViewModel getPackageViewModel() {
        return packageViewModel;
    }

    public void setPackageViewModel(PackageViewModel packageViewModel) {
        this.packageViewModel = packageViewModel;
    }
}
