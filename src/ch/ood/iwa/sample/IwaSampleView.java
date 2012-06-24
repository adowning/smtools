package ch.ood.iwa.sample;

import java.io.Serializable;

import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.i18n.Lang;

import ch.ood.iwa.module.presenter.UsersPresenter;
import ch.ood.iwa.module.presenter.util.UserFormFieldFactory;
import ch.ood.iwa.module.presenter.util.UsersContainer;
import ch.ood.iwa.module.ui.AbstractModuleView;
import ch.ood.iwa.ui.UiFactory;

import com.fifthfloor.gps.server.objects.Company;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

/**
 * View that handles Users
 *
 * @author Mischa
 *
 */
public class IwaSampleView
                                extends AbstractModuleView<CustomLayout,  IwaSamplePresenter, IwaSamplePresenter.UI>
                                implements IwaSamplePresenter.UI, Serializable {

        private static final long serialVersionUID = 1L;
        private Label label;
        private Table table;    
        private Form form = new Form();
        private Button btnNew;          
        private Button btnSave;
        private Button btnDelete;
       
        public IwaSampleView() {          
        	
                // Common View Settings
                super( "Sample", new CustomLayout("SimpleTableFormLayout"), new IwaSamplePresenter());
                System.out.println("i hit");
                getPresenter().setUi(this);            
                initLayout();
                getPresenter().init();
       
                // Select first Entry (must happen after we initialized the listeners)
                table.select(table.firstItemId());
        }
               
        private void initLayout() {
                label = new Label(Lang.getMessage("Companies"));
                getContent().addComponent(label, "label");                                      
                initTable();
                initForm();
        }
       
        private void initTable() {              
                table = new Table(null, getPresenter().getCompaniesContainer());            
                table.setWidth("100%");
                table.setHeight("240px");
                table.setVisibleColumns(CompaniesContainer.getVisibleColumns());
                //TODO set fix here since companycontainer doesnt super ?
                table.setColumnHeaders((CompaniesContainer.getColumnCaptions()));
                table.setSelectable(true);
                table.setNullSelectionAllowed(false);
                table.setImmediate(true);                              
                getContent().addComponent(table, "table");
        }      

        private void initForm() {
                btnNew = UiFactory.createButton("New");
                btnDelete = UiFactory.createButton("Delete");
                btnSave = UiFactory.createButton("Save");
               
                form.setFormFieldFactory(new CompanyFormFieldFactory());
               
                /**
                 * Important because of the User.getPasswordAsString always
                 * returning an empty string
                 */            
                form.setImmediate(false);
                form.setWriteThrough(false);

                getContent().addComponent(btnNew, "btnNew");    
                form.getFooter().addComponent(btnDelete);
                form.getFooter().addComponent(btnSave);        
               
                ((HorizontalLayout)form.getFooter()).setSpacing(true);          
                               
                getContent().addComponent(form, "form");
               
                // Set a dummy value to have the form displayed in any case
                form.setItemDataSource(new BeanItem<Company>(new Company()));
                form.setVisibleItemProperties(CompanyFormFieldFactory.getVisibleFields());
        }
       
        @Override
        public void activated(Object... params) {
                super.activated(params);
                getPresenter().activated(params);
        }      
       
        @Override
        public boolean isFullScreen() {
                return false;
        }

        @Override
        public Table getTable() {
                return table;
        }

        @Override
        public Form getForm() {
                return form;
        }

        @Override
        public Button getBtnNew() {
                return btnNew;
        }

        @Override
        public Button getBtnSave() {
                return btnSave;
        }

        @Override
        public Button getBtnDelete() {
                return btnDelete;
        }
}
