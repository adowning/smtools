package ch.ood.iwa.sample;


import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.fifthfloor.gps.server.objects.Company;
import com.fifthfloor.gps.server.objects.Driver;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.addon.jpacontainer.provider.CachingMutableLocalEntityProvider;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class JPAContainerExample extends CustomComponent  {
    private static final long serialVersionUID = -3205020480634478985L;
    String context;

    public void init(String context) {
        VerticalLayout layout = new VerticalLayout();
        
        if ("nonpersistent".equals(context))
            nonpersistent(layout);
        else if ("basic".equals(context))
            basic(layout);
        else if ("thehardway".equals(context))
            thehardway(layout);
        else if ("buffering".equals(context))
            buffering(layout);
        else if ("nested".equals(context))
            nested(layout);
        
        setCompositionRoot(layout);
    }
    
    // BEGIN-EXAMPLE: jpacontainer.nonpersistent
    /* Comets are not persistent */
    public class Comet {
        int id;
        String name;
        
        public Comet(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
    
    void nonpersistent(VerticalLayout layout) {
        BeanItemContainer<Comet> comets = new BeanItemContainer<Comet>(Comet.class);
        
        comets.addBean(new Comet(1, "Halley"));
        comets.addBean(new Comet(2, "Encke"));
        comets.addBean(new Comet(3, "Biela"));
        comets.addBean(new Comet(4, "Faye"));
        
        Table cometTable = new Table("The Comets", comets);
        // END-EXAMPLE: jpacontainer.nonpersistent
        cometTable.setPageLength(cometTable.size());
        layout.addComponent(cometTable);
    }

    public static final String basicDescription =
        "<h1>Basic Use of JPAContainer</h1>"+
        "<p>The easiest way to create a <b>JPAContainer</b> is to use the <b>JPAContainerFactory</b>.</p>";

    void basic(Layout layout) {
        // EXAMPLE-REF: jpacontainer.basic com.vaadin.book.examples.addons.jpacontainer.Person jpacontainer.basic
        // BEGIN-EXAMPLE: jpacontainer.basic
        // Let's have some data created with pure JPA
        EntityManager em = JPAContainerFactory.
            createEntityManagerForPersistenceUnit("book-examples");
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Person p").executeUpdate();
        em.persist(new Driver("James", "123123123"));
        em.persist(new Driver("Sarah Knauss", "v"));
        em.persist(new Driver("Lucy Hannah", "v"));
        em.getTransaction().commit();
        
        // Create a persistent person container
        JPAContainer<Driver> persons =
            JPAContainerFactory.make(Driver.class, "book-examples");

        // You can add entities to the container as well
        persons.addEntity(new Driver("Marie-Louise Meilleur", "V"));
        
        // Bind it to a component
        Table personTable = new Table("The Persistent People", persons);
        personTable.setVisibleColumns(new String[]{"id","name","vehicle"});
        layout.addComponent(personTable);
        // END-EXAMPLE: jpacontainer.basic

        // Set up sorting
        persons.sort(new String[]{"age", "vehicle"},
                     new boolean[]{false, false});

        personTable.setPageLength(4);
        layout.addComponent(personTable);
    }

    public static final String thehardwayDescription =
        "<h1>The Hard Way to Create a JPAContainer</h1>"+
        "<p>You need the following steps:</p>" +
        "<ol>" +
        "  <li>Create an <b>EntityManagerFactory</b></li>" +
        "  <li>Create an <b>EntityManager</b></li>" +
        "  <li>Create an entity provider</li>" +
        "  <li>Create the <b>JPAContainer</b></li>" +
        "  <li>Use the entity provider in the <b>JPAContainer</b></li>" +
        "</ol>";

    void thehardway(Layout layout) {
        // BEGIN-EXAMPLE: jpacontainer.thehardway
        // We need a factory to create entity manager
        EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("book-examples");
        
        // We need an entity manager to create entity provider
        EntityManager em = emf.createEntityManager();
        
        // We need an entity provider to create a container        
        CachingMutableLocalEntityProvider<Driver> entityProvider =
            new CachingMutableLocalEntityProvider<Driver>(Driver.class,
                                                          em);
        
        // And there we have it
        JPAContainer<Driver> persons =
                new JPAContainer<Driver> (Driver.class);
        persons.setEntityProvider(entityProvider);
        
        // We have to empty it for the purpose of the example -
        // the items are really persistent.
        for (Object itemId: persons.getItemIds())
            persons.removeItem(itemId);
        
        // Add some items to it
        persons.addEntity(new Driver("Jeanne Calment", "asdfs"));
        persons.addEntity(new Driver("Sarah Knauss",  "asdfa"));
        persons.addEntity(new Driver("Lucy Hannah",  "asdfw"));
        persons.addEntity(new Driver("Marie-Louise Meilleur",  "asdddf"));
        
        // Bind it to a component
        Table personTable = new Table("The Persistent People", persons);
        // END-EXAMPLE: jpacontainer.thehardway

        personTable.setPageLength(4);
        layout.addComponent(personTable);
    }

    public static final String bufferingDescription =
            "<h1>Container Buffering</h1>"+
            "<p>You can enable container-level buffering with <tt>setAutoCommit(false)</tt>. " +
            "To commit changes, call <tt>commit()</tt> for the container.</p>";

    void buffering(Layout layout) {
        // BEGIN-EXAMPLE: jpacontainer.buffering
        // Let's have some data created with pure JPA
        EntityManager em = JPAContainerFactory.
            createEntityManagerForPersistenceUnit("book-examples");
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Person p").executeUpdate();
        em.persist(new Driver("Jeanne Calment", "asdfdsf"));
        em.persist(new Driver("Sarah Knauss", "asdfdsfssss"));
        em.persist(new Driver("Lucy Hannah", "asdfdwwwwsf"));
        em.getTransaction().commit();
        
        // Create a persistent person container
        final JPAContainer<Driver> persons =
            JPAContainerFactory.makeBatchable(Driver.class, "book-examples");
        // persons.setWriteThrough(false);
        persons.setAutoCommit(false);

        // Bind it to a component
        final Table personTable = new Table(null, persons);
        personTable.setVisibleColumns(new String[]{"id","name","age"});
        
        final Form editor = new Form();
        editor.setFormFieldFactory(new FieldFactory());
        editor.setWriteThrough(false);

        final Panel editorPanel = new Panel("Editor");
        editorPanel.setVisible(false);
        editorPanel.addComponent(editor);

        final Button add = new Button("Add New");
        add.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                persons.createEntityItem(new Driver(null, ""));
                Object itemId = persons.addItem();
                add.setData(itemId);
                editor.setItemDataSource(persons.getItem(itemId));
                editorPanel.setVisible(true);
            }
        });

        Button save = new Button("Save");
        save.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                editor.commit();
                persons.commit();
                editor.setItemDataSource(persons.getItem(add.getData()));
                // persons.refresh();
            }
        });
        editor.getFooter().addComponent(save);

        Button cancel = new Button("Cancel");
        cancel.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                editor.discard();
                persons.discard();
                editorPanel.setVisible(false);
            }
        });
        editor.getFooter().addComponent(cancel);

        HorizontalLayout horizontal = new HorizontalLayout();
        Panel masterPanel = new Panel("Drivers");
        masterPanel.addComponent(personTable);
        masterPanel.addComponent(add);
        horizontal.addComponent(masterPanel);
        horizontal.addComponent(editorPanel);
        // END-EXAMPLE: jpacontainer.bufferinc

        // Set up sorting
        persons.sort(new String[]{"age", "vehicle"},
                     new boolean[]{false, false});

        personTable.setPageLength(4);
        layout.addComponent(horizontal);
    }

    /*************************************************************************
     * Nested Properties
     *************************************************************************/
    public static final String nestedDescription =
        "<h1>Nested Properties</h1>"+
        "<p></p>";

    void nested(Layout layout) {
        // Populate with example data
        JPAContainerExample.insertExampleData();
        
        // BEGIN-EXAMPLE: jpacontainer.nested
        // Have a persistent container
        JPAContainer<Driver> persons =
            JPAContainerFactory.make(Driver.class, "book-examples");

        // Add a nested property to a many-to-one property
        persons.addNestedContainerProperty("country.name");
        
        // Show the persons in a table, except the "country" column,
        // which is an object - show the nested property instead
        Table personTable = new Table("The Persistent People", persons);
        personTable.setVisibleColumns(new String[]{"name","age",
                                                   "company.name"});

        // Have a nicer caption for the country.name column
        personTable.setColumnHeader("company.name", "Nationality");
        // END-EXAMPLE: jpacontainer.nested

        // Set up sorting
        persons.sort(new String[]{"age", "vehicle"},
                     new boolean[]{false, false});
        
        personTable.setPageLength(5);
        layout.addComponent(personTable);
    }

    static HashMap<String,Company> insertExampleDataWithJPAContainer(JPAContainer<Driver> persons, JPAContainer<Company> countries) {
        // We have to empty it for the purpose of the example -
        // the items are really persistent.
        for (Object itemId: persons.getItemIds())
            persons.removeItem(itemId);
        for (Object itemId: countries.getItemIds())
            countries.removeItem(itemId);
        
        // Add some items to the containers
        
        // Create a new entity and add it to a container        
        Company france = new Company();
        france.setCname("France");
        Object itemId = countries.addEntity(france);
        
        // Get the managed entity
        france = countries.getItem(itemId).getEntity();

        // Use the managed entity in entity references
        persons.addEntity(new Driver("Jeanne Calment", "asdf", france));
        
        // Add a bunch of other items
        HashMap<String,Company> countryMap = new HashMap<String, Company>();
        for (String s: new String[]{"United States", "Canada", "Ecuador", "Japan"}) {
        	Company c = new Company();
        	c.setCname(s);
            Object id = countries.addEntity(c);
            countryMap.put(s, countries.getItem(id).getEntity());
        }
        
        persons.addEntity(new Driver("Sarah Knauss", "asdf", countryMap.get("United States")));
        persons.addEntity(new Driver("Lucy Hannah", "asdf", countryMap.get("United States")));
        persons.addEntity(new Driver("Marie-Louise Meilleur", "asdf", countryMap.get("Canada")));
        persons.addEntity(new Driver("María Capovilla", "asdf", countryMap.get("Ecuador")));
        persons.addEntity(new Driver("Tane Ikai", "asdf", countryMap.get("Japan")));
        persons.addEntity(new Driver("Elizabeth Bolden", "asdf", countryMap.get("United States")));
        persons.addEntity(new Driver("Carrie White", "asdf", countryMap.get("United States")));
        persons.addEntity(new Driver("Kamato Hongo", "asdf", countryMap.get("Japan")));
        persons.addEntity(new Driver("Maggie Barnes", "asdf", countryMap.get("United States")));
        
        return countryMap;
    }

    static void insertExampleData() {
        /*
        // Check if there already is data
        EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("book-examples");
        Query q = em.createQuery("SELECT COUNT(p.id) FROM Person p");
        int count = (Integer) q.getSingleResult();
        System.out.println("First result= " + count);
        if (count > 0)
            return;
            */

        // Let's have some data created with using pure JPA
        EntityManager em = JPAContainerFactory.
            createEntityManagerForPersistenceUnit("book-examples");
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Person p").executeUpdate();
        em.createQuery("DELETE FROM Country c").executeUpdate();
        
        // Add the countries
        HashMap<String,Company> countryMap = new HashMap<String, Company>();
        for (String s: new String[]{"France", "United States", "Canada", "Ecuador", "Japan"}) {
        	Company c = new Company();
        	c.setCname(s);
            em.persist(c);
            countryMap.put(s, c);
        }
        
        em.persist(new Driver("Jeanne Calment", "asdf", countryMap.get("France")));
        em.persist(new Driver("Sarah Knauss", "asdf", countryMap.get("United States")));
        em.persist(new Driver("Lucy Hannah", "asdf", countryMap.get("United States")));
        em.persist(new Driver("Marie-Louise Meilleur", "asdf", countryMap.get("Canada")));
        em.persist(new Driver("María Capovilla", "asdf", countryMap.get("Ecuador")));
        em.persist(new Driver("Tane Ikai", "asdf", countryMap.get("Japan")));
        em.persist(new Driver("Elizabeth Bolden", "asdf", countryMap.get("United States")));
        em.persist(new Driver("Carrie White", "asdf", countryMap.get("United States")));
        em.persist(new Driver("Kamato Hongo", "asdf", countryMap.get("Japan")));
        em.persist(new Driver("Maggie Barnes", "asdf", countryMap.get("United States")));
        em.getTransaction().commit();
    }
}