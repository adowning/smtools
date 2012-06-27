package ch.ood.iwa.sample;


import com.fifthfloor.gps.server.objects.Driver;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.MasterDetailEditor;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class JPAFieldFactoryExample extends CustomComponent {
    private static final long serialVersionUID = -3205020480634478985L;
    String context;

    public void init(String context) {
        VerticalLayout layout = new VerticalLayout();
        
        if ("masterdetail".equals(context))
            masterdetail(layout);
        else if ("formonetoone".equals(context))
            formonetomany(layout);
        
        setCompositionRoot(layout);
    }
    

    public static final String formonetomanyDescription =
            "<h1>Form with a One-to-Many Relationship</h1>"+
            "<p>The <b>Country</b> has <tt>@OneToMany</tt> relationship with the <b>Person</b> entity type " +
            "so the <b>FieldFactory</b> creates a <b>MasterDetailEditor</b> to edit it.</p>";

    void formonetomany(VerticalLayout layout) {
        // Populate with example data
        JPAContainerExample.insertExampleData();
        
        // BEGIN-EXAMPLE: jpacontainer.fieldfactory.formonetoone
        // Have a persistent container
        final JPAContainer<Driver> countries =
            JPAContainerFactory.make(Driver.class, "book-examples");

        // For selecting an item to edit
        final Select countrySelect = new Select("Select a Country",
                                                countries);
        countrySelect.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        countrySelect.setItemCaptionPropertyId("name");

        // Country Editor
        final Form  countryForm  = new Form();
        countryForm.setCaption("Country Editor");
        countryForm.addStyleName("bordered"); // Custom style
        countryForm.setWidth("420px");
        countryForm.setWriteThrough(false); // Enable buffering
        countryForm.setEnabled(false);

        // When an item is selected from the list...
        countrySelect.addListener(new ValueChangeListener() {
            private static final long serialVersionUID = 3371750143781493244L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                // Get the item to edit in the form
                Item countryItem =
                    countries.getItem(event.getProperty().getValue());
                
                // Use a JPAContainer field factory
                //  - no configuration is needed here
                final FieldFactory fieldFactory = new FieldFactory();
                countryForm.setFormFieldFactory(fieldFactory);

                // Edit the item in the form
                countryForm.setItemDataSource(countryItem);
                countryForm.setEnabled(true);
                
                // Handle saves on the form
                final Button save = new Button("Save");
                countryForm.getFooter().removeAllComponents();
                countryForm.getFooter().addComponent(save);
                save.addListener(new ClickListener() {
                    private static final long serialVersionUID = 3147385792741616617L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        try {
                            countryForm.commit();
                            countryForm.setEnabled(false);
                        } catch (InvalidValueException e) {
                        }
                    }
                });
            }
        });
        countrySelect.setImmediate(true);
        countrySelect.setNullSelectionAllowed(false);
        // END-EXAMPLE: jpacontainer.fieldfactory.formonetoone

        layout.setSpacing(true);
        layout.addComponent(countrySelect);
        layout.addComponent(countryForm);
    }

    public static final String masterdetailDescription =
        "<h1>Master-Detail Editor for a Property</h1>"+
        "<p>The easiest way to create a <b>JPAContainer</b> is to use the <b>JPAContainerFactory</b>.</p>";

    void masterdetail(Layout layout) {
        // Populate with example data
        JPAContainerExample.insertExampleData();

        // BEGIN-EXAMPLE: jpacontainer.fieldfactory.masterdetail
        // Create persistent containers
        final JPAContainer<Driver> countries =
            JPAContainerFactory.make(Driver.class, "book-examples");

        // Create, configure, and use a field factory
        final FieldFactory fieldFactory = new FieldFactory();

        // A table to display the country list
        Panel masterPanel = new Panel("Master Table");
        final Table masterTable = new Table("Select One",
                                             countries);
        masterTable.setVisibleColumns(new String[]{"name"});
        masterPanel.addComponent(masterTable);

        // Have a placeholder for the editor
        final Panel detailPanel = new Panel("The Details");

        // When an item is selected from the table...
        masterTable.addListener(new ValueChangeListener() {
            private static final long serialVersionUID = 3371750143781493244L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                // Create the editor
                MasterDetailEditor editor =
                    new MasterDetailEditor(fieldFactory, countries,
                        event.getProperty().getValue(),
                        "people", detailPanel);
                
                // Make the editor visible
                detailPanel.setVisible(true);
                detailPanel.removeAllComponents();
                detailPanel.addComponent(editor);
            }
        });
        masterTable.setSelectable(true);
        masterTable.setImmediate(true);
        masterTable.setNullSelectionAllowed(false);
        // END-EXAMPLE: jpacontainer.fieldfactory.masterdetail

        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setSpacing(true);
        masterTable.setPageLength(5);
        hlayout.addComponent(masterPanel);
        hlayout.addComponent(detailPanel);
        layout.addComponent(hlayout);
    }
}