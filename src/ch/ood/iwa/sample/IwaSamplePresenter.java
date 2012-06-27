
package ch.ood.iwa.sample;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.vaadin.appfoundation.authentication.util.PasswordUtil;
import org.vaadin.appfoundation.i18n.Lang;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;
import org.vaadin.dialogs.ConfirmDialog;

import ch.ood.iwa.IwaPersistenceHelper;
import ch.ood.iwa.module.presenter.AbstractModulePresenter;
import ch.ood.iwa.module.ui.IwaModuleUI;

import com.fifthfloor.gps.server.objects.Company;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table;

/**
 * Presenter for the Companys View
 *
 * @author Mischa
 *
 */
public class IwaSamplePresenter extends AbstractModulePresenter<IwaSamplePresenter.UI> implements Serializable {

        private static final long serialVersionUID = 1L;
        private CompaniesContainer compContainer;
       
        /**
         * This is the interface that decouples Presenter and View
         *
         */
        public static interface UI extends IwaModuleUI {                
                Table getTable();
                
                Form getForm();        
                Button getBtnNew();
                Button getBtnDelete();
                Button getBtnSave();               
                
        }

        /**
         * Constructor
         */
        public IwaSamplePresenter() {              
                if (compContainer == null) {
                	compContainer = new CompaniesContainer();
                	compContainer.populateContainer();                    
                }                                      
        }
               
        /**
         * Initializes the presenter
         */
        public void init() {            
                initListeners();
                refreshView();
        }
       
        private void initListeners() {          
                getUi().getTable().addListener(tableValueChangeListener);
                getUi().getBtnNew().addListener(buttonClickListener);
                getUi().getBtnDelete().addListener(buttonClickListener);
                getUi().getBtnSave().addListener(buttonClickListener);    
                
                
        }      
       
        /**
         * Value Change Listener for the Table
         */    
        private ValueChangeListener tableValueChangeListener = new ValueChangeListener() {                              
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(ValueChangeEvent event) {                      
                        synchronizeFormWithTable();
                        refreshView();
                }
        };
       
        private BeanItem<Company> getSelectedCompanyItem() {
                Object selectedItemId = getUi().getTable().getValue();  
                System.out.println(selectedItemId);
                @SuppressWarnings("unchecked")
                BeanItem<Company> companyBeanItem = (BeanItem<Company>)getUi().getTable().getItem(selectedItemId);
                return companyBeanItem;
        }
       
        private Company getSelectedCompany () {
                if (getSelectedCompanyItem() != null) {
                        return getSelectedCompanyItem().getBean();
                } else {
                        return null;
                }
        }
               
        /**
         * Button Click Listener
         */
        private ClickListener buttonClickListener = new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {                                            
                        if (event.getButton().equals(getUi().getBtnNew())) {
                                handleNewButtonClicked();
                        } else if (event.getButton().equals(getUi().getBtnDelete())) {
                                handleDeleteButtonClicked();
                        } else if (event.getButton().equals(getUi().getBtnSave())) {
                                handleSaveButtonClicked();
                        }
                }
        };
        

               
        private void synchronizeFormWithTable() {
                if (getSelectedCompanyItem() != null) {
                        getUi().getForm().setItemDataSource(getSelectedCompanyItem());                                            
                } else {
                        // set a dummy bean item to prevent the fields from disappearing
                        getUi().getForm().setItemDataSource(new BeanItem<Company>(new Company()));                    
                }
                // must happen after setting the datasource...
                getUi().getForm().setVisibleItemProperties(CompanyFormFieldFactory.getVisibleFields());            
        }
       
        void handleNewButtonClicked() {
                // Unselect table
                getUi().getTable().select(getUi().getTable().getNullSelectionItemId());
               
                // set a fresh new bean item
                getUi().getForm().setItemDataSource(new BeanItem<Company>(new Company()));                    
       
                // must happen after setting the datasource...
                getUi().getForm().setVisibleItemProperties(CompanyFormFieldFactory.getVisibleFields());
               
                getUi().getBtnSave().setEnabled(true);
        }
        

       
        void handleDeleteButtonClicked() {
                getUi().showConfirmation("", Lang.getMessage("ConfirmDeleteMsg"), new ConfirmDeleteCompanyDialogListener());
        }
       
        void deleteCompany() {                    
                // Cache the current items neighbours
                Company selectedCompany = getSelectedCompany();
               System.out.println("in delete:"+selectedCompany);
                if (selectedCompany == null) return;
               
                Object nextNeighbour = getUi().getTable().nextItemId(selectedCompany);
                Object previousNeighbour = getUi().getTable().prevItemId(selectedCompany);
               
                // remove from store
                FacadeFactory.getFacade().delete(selectedCompany);
               
                // remove from container, refreshes the view
                compContainer.removeItem(selectedCompany);
               
                // Select some other row
                if (nextNeighbour != null) {
                        getUi().getTable().select(nextNeighbour);
                } else if (previousNeighbour != null){
                        getUi().getTable().select(previousNeighbour);
                } else if (getUi().getTable().size() == 0) {
                        getUi().getTable().select(null);
                       
                } else {
                        getUi().getTable().select(getUi().getTable().firstItemId());
                }                              
        }      
       
        void handleSaveButtonClicked() {
                getUi().getForm().commit();            
                @SuppressWarnings("unchecked")
                BeanItem<Company> companyBeanItem = (BeanItem<Company>) getUi().getForm().getItemDataSource();
                Company company = companyBeanItem.getBean();
               
                /**
                 * Avoid duplicate entry for the same email address.
                 * This has to be checked programatically as GAE does not
                 * support unique constraints
                 */
                if (company.getId() == null  && isCompanyExisting(company.getCname())) {
                        getUi().showError(Lang.getMessage("DuplicateEntryMsg", "cname"), null);
                        return;
                }              
               
                // Encrypt and set the password, only if a new one is given
                //TODO fix me
                if (company.getNewPassword() != null && company.getNewPassword().length() > 0) {
                        String hashedPassword = PasswordUtil.generateHashedPassword(company.getNewPassword());
                        company.setPassword(hashedPassword);
                }

                try {
                        // Store the Company
                        FacadeFactory.getFacade().store(company);
                       
                } catch (Exception e) {
                        getUi().showError(Lang.getMessage("ChangesSaveFailedMsg") , "Details: " + e.getMessage());
                }              
                updateTable();
        }      
               
        /**
         * Checks whether a Company with that given email address is
         * already in the DB
         *
         * @param email
         * @return
         */
        private boolean isCompanyExisting(String cname) {          
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("cname", cname);

                Company c = FacadeFactory.getFacade().find("SELECT c FROM Company c WHERE c.cname = :cname", params);
               
                if (c == null) {
                        return false;
                } else {
                        return true;
                }
        }              
       
        public CompaniesContainer getCompaniesContainer() {
                return compContainer;
        }
               
        public void activated(Object... params) {
                updateTable();
        }
       
        private void updateTable() {   
        	//TODO fix me
//                Company selectedCompany = getSelectedCompany();
//               
//                if (selectedCompany == null) {                    
//                        @SuppressWarnings("unchecked")
//                        BeanItem<Company> beanItem = (BeanItem<Company>)getUi().getForm().getItemDataSource();
//                        selectedCompany = beanItem.getBean();                      
//                }
//               
//                if (selectedCompany == null) {
//                        return;
//                }
//                       
//                // Updates table
//                compContainer.populateContainer();    
//               
//                // Get the current id
//                selectedCompany = new IwaPersistenceHelper().refreshCompany(selectedCompany);
//               
//                // Select row
//                getUi().getTable().select(selectedCompany);                                                
        }

        private void refreshView() {
                if (getSelectedCompany() == null) {
                        getUi().getBtnNew().setEnabled(true);
                        getUi().getBtnDelete().setEnabled(false);
                        getUi().getBtnSave().setEnabled(false);                
                } else {
                        getUi().getBtnNew().setEnabled(true);
                        getUi().getBtnDelete().setEnabled(true);
                        getUi().getBtnSave().setEnabled(true);                  
                }
        }
       
        /**
         * Little helper, can not be implemented anonymously for GAE requires it to be Serializable.
         * It is default scoped for test reasons.
         */
        public class ConfirmDeleteCompanyDialogListener implements ConfirmDialog.Listener, Serializable {
                private static final long serialVersionUID = 1L;
                @Override
                public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                                deleteCompany();
                        }
                }
        }              
}
