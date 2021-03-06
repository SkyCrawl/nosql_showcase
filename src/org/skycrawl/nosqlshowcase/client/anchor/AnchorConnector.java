package org.skycrawl.nosqlshowcase.client.anchor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Anchor;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @see {@link org.pikater.web.vaadin.gui.server.components.anchor.Anchor}
 * @author SkyCrawl
 */
@Connect(org.skycrawl.nosqlshowcase.server.root.ui.anchor.Anchor.class)
public class AnchorConnector extends AbstractComponentConnector
{
	private static final long		serialVersionUID	= 5557617858638230393L;

	private final AnchorServerRpc	rpc					= RpcProxy.create(AnchorServerRpc.class, this);
	private HandlerRegistration		clickHandlerRegistration;

	public AnchorConnector()
	{
		this.clickHandlerRegistration = null;

		registerRpc(AnchorClientRpc.class, new AnchorClientRpc()
		{
			private static final long	serialVersionUID	= -8432862667383416641L;
		});
	}

	@Override
	protected Anchor createWidget()
	{
		return GWT.create(Anchor.class);
	}

	@Override
	public Anchor getWidget()
	{
		return (Anchor) super.getWidget();
	}

	@Override
	public AnchorState getState()
	{
		return (AnchorState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent)
	{
		super.onStateChanged(stateChangeEvent);
		
		// set content of the anchor
		if(getState().content != null)
		{
			if(getState().content.isHTML)
			{
				getWidget().setHTML(SafeHtmlUtils.fromString(getState().content.text));
			}
			else
			{
				getWidget().setText(getState().content.text);
			}
		}
		
		// and handle clicks
		if (getState().forwardClickToServer)
		{
			/*
			 * Clicks on the GWT anchor widget will delegate to the server.
			 */

			getWidget().getElement().removeAttribute("href"); // no client-side handling of the click
			if (clickHandlerRegistration == null)
			{
				clickHandlerRegistration = getWidget().addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						if (isEnabled())
						{
							rpc.clicked(MouseEventDetailsBuilder.buildMouseEventDetails(event.getNativeEvent(), getWidget().getElement()));
						}
					}
				});
			}
		}
		else
		{
			/*
			 * Clicks on the GWT anchor widget will NOT delegate to the server.
			 */

			if (clickHandlerRegistration != null)
			{
				clickHandlerRegistration.removeHandler();
				clickHandlerRegistration = null;
			}
			getWidget().getElement().setAttribute("href", getState().hrefAttrContent); // pure client-side handling of the click
		}
	}
}