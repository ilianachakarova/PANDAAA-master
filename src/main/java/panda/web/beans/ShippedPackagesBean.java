package panda.web.beans;

import org.modelmapper.ModelMapper;
import panda.domain.entities.Status;
import panda.domain.models.view.PackageViewModel;
import panda.service.PackageService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class ShippedPackagesBean {
    private List<PackageViewModel> shippedPackages;

    private ModelMapper modelMapper;
    private PackageService packageService;

    public ShippedPackagesBean() {
    }
    @Inject
    public ShippedPackagesBean(ModelMapper modelMapper, PackageService packageService) {
        this.modelMapper = modelMapper;
        this.packageService = packageService;
        this.initPackages();
    }

    private void initPackages() {
        this.shippedPackages = this.packageService.findAllPackagesByStatus(Status.Shipped)
                .stream()
                .map(p -> {
                    PackageViewModel packageViewModel = this.modelMapper.map(p, PackageViewModel.class);
                    packageViewModel.setRecipient(p.getRecipient().getUsername());
                    packageViewModel.setEstimatedDeliveryTime(p.getEstimatedDeliveryTime().toString());
                    return packageViewModel;
                })
                .collect(Collectors.toList());

    }

    public List<PackageViewModel> getShippedPackages() {
        return shippedPackages;
    }

    public void setShippedPackages(List<PackageViewModel> shippedPackages) {
        this.shippedPackages = shippedPackages;
    }

    public void changeStatus(String id) throws IOException {
        this.packageService.packageStatusChange(id);

        FacesContext.getCurrentInstance().getExternalContext().redirect("/faces/view/admin/shipped.xhtml");
    }
}
