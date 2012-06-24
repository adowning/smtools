//package ch.ood.iwa.sample;
//
//import java.io.Serializable;
//
//import ch.ood.iwa.module.ui.AbstractModuleView;
//
//import com.vaadin.ui.Button;
//import com.vaadin.ui.VerticalLayout;
//
///**
// * Demonstrates how to implement a "full screen" (i.e. no header
// * or navigation panels).
// *  
// * @author Mischa
// *
// */
//public class IwaSampleFullScreenView 
//				extends AbstractModuleView<VerticalLayout, IwaSamplePresenter, IwaSamplePresenter.UI>
//				implements IwaSamplePresenter.UI, Serializable {
//
//	private static final long serialVersionUID = 1L;
//	private Button btnHello;		
//	
//	public IwaSampleFullScreenView() {		
//		super("Sample", new VerticalLayout(), new IwaSamplePresenter());
//		getPresenter().setUi(this);		
//		initLayout();
//		getPresenter().initListeners(); 		
//	}
//		
//	private void initLayout() {
//		btnHello = new Button("Say Hello");
//		getContent().addComponent(btnHello);					
//	}
//	
//	@Override
//	public boolean isFullScreen() {
//		return true;
//	}
//
//	@Override
//	public Button getHelloButton() {
//		return btnHello;
//	}
//}
