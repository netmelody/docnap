package org.netmelody.docnap.gwt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class TagsMenu extends Composite {

    public interface Images extends ClientBundle, Tree.Resources {
        ImageResource tag();
        @Source("blank.png")
        ImageResource treeLeaf();
    }

    private Tree tree;
    private ImageResource tagImage;

    public TagsMenu() {
        final Images images = GWT.create(Images.class);
        tagImage = images.tag();
        tree = new Tree(images);

        final TreeItem root = new TreeItem(imageItemHTML(images.tag(), "All"));
        tree.addItem(root);
        
        root.setState(true);
        initWidget(tree);
        
        tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
            @Override
            public void onSelection(SelectionEvent<TreeItem> event) {
                if (root == event.getSelectedItem()) {
                    return;
                }
            }
        });
        
    }

    private String imageItemHTML(ImageResource imageProto, String title) {
        return AbstractImagePrototype.create(imageProto).getHTML() + " " + title;
    }
}
