package ch.ood.iwa.sample;

import org.vaadin.appfoundation.i18n.Lang;

import ch.ood.iwa.IwaUtil;

import com.vaadin.data.Validator;

/**
 * Vaadin Form Validator. There is an email validator
 * in the Vaadin Framework existing, this is to demonstrate
 * how to implement an own validator
 *
 * @author Mischa
 *
 */
public class EmailFieldValidator implements Validator {
       
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void validate(Object value) throws InvalidValueException {              
                if (value == null || !(value instanceof String)) {
                        return;
                }
                String email = (String)value;
                if (new IwaUtil().isValidEmailAddress(email) == false) {
                        throw new InvalidValueException(Lang.getMessage("InvalidEmailMsg", email));
                }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isValid(Object value) {
                if (value == null || !(value instanceof String)) {
                        return false;
                }
                String email = (String)value;          
                return new IwaUtil().isValidEmailAddress(email);
        }
}

