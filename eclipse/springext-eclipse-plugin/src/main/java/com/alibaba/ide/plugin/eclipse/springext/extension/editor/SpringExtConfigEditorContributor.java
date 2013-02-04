package com.alibaba.ide.plugin.eclipse.springext.extension.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

import com.alibaba.ide.plugin.eclipse.springext.SpringExtPlugin;
import com.alibaba.ide.plugin.eclipse.springext.util.dom.DomDocumentUtil;

public class SpringExtConfigEditorContributor extends MultiPageEditorActionBarContributor {
    private final static String GROUP_ID = SpringExtConfigEditor.EDITOR_ID;
    private SpringExtConfig config;
    private RemoveUnusedNamespacesAction removeUnusedNamespacesAction;

    @Override
    public void contributeToToolBar(IToolBarManager toolBarManager) {
        removeUnusedNamespacesAction = new RemoveUnusedNamespacesAction();

        toolBarManager.add(new GroupMarker(GROUP_ID));
        toolBarManager.appendToGroup(GROUP_ID, removeUnusedNamespacesAction);
    }

    @Override
    public void setActiveEditor(IEditorPart part) {
        if (part instanceof SpringExtConfigEditor) {
            config = ((SpringExtConfigEditor) part).getConfig();
        }

        super.setActiveEditor(part);
    }

    @Override
    public void setActivePage(IEditorPart activeEditor) {
        removeUnusedNamespacesAction.setEnabled(true);
    }

    private class RemoveUnusedNamespacesAction extends Action {
        public RemoveUnusedNamespacesAction() {
            super("Remove Unused Namespaces", Action.AS_PUSH_BUTTON);
            setImageDescriptor(SpringExtPlugin.getDefault().getImageRegistry().getDescriptor("clear"));
        }

        @Override
        public void run() {
            if (config != null) {
                DomDocumentUtil.removeUnusedNamespaceDefinitions(config);
                config.getTextViewer().refresh();
            }
        }
    }
}
