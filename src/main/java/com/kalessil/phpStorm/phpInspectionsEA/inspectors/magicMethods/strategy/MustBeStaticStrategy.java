package com.kalessil.phpStorm.phpInspectionsEA.inspectors.magicMethods.strategy;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.Method;
import com.kalessil.phpStorm.phpInspectionsEA.utils.NamedElementUtil;

public class MustBeStaticStrategy {
    private static final String strProblemDescription = "%m% must be static.";

    static public void apply(final Method method, final ProblemsHolder holder) {
        if (!method.isStatic()) {
            final PsiElement nameNode = NamedElementUtil.getNameIdentifier(method);
            if (nameNode != null) {
                final String strMessage = strProblemDescription.replace("%m%", method.getName());
                holder.registerProblem(nameNode, strMessage, ProblemHighlightType.ERROR);
            }
        }
    }
}
