package ch.ood.iwa.sample;

import java.io.Serializable;
import java.util.List;

import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;

import com.fifthfloor.gps.server.objects.Company;
import com.vaadin.data.util.BeanItemContainer;

/**
 * A specialized BeanItemContainer for Users, used by Tables
 * 
 * @author Mischa
 *
 */
public class CompaniesContainer extends BeanItemContainer<Company> implements Serializable {
        
        private static final long serialVersionUID = 1L;
        //TODO add vehicles list to this
        private static final Object[] VISIBLE_COLUMNS = new Object[] {
                "cname", "owner", "email" };
    private static final String[] COL_HEADERS = new String[] {"Companyname", "Owner", "Email"};
        
    /**
     * Default constructor
     */
        public CompaniesContainer() {
                super(Company.class);              
        }

        public static Object[] getVisibleColumns() {
                return VISIBLE_COLUMNS;
        }
        
        public static String[] getColumnCaptions() {
                return COL_HEADERS;
        }
        
        public void populateContainer() {                               
                removeAllItems();

                List<Company> results = FacadeFactory.getFacade().list(Company.class);
                                                  
                if (results != null) {
                        for (Company c : results) {                     
                                addItem(c);
                        }
                }
        }
}
