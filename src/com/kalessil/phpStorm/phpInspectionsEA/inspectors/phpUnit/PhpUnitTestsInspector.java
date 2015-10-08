package com.kalessil.phpStorm.phpInspectionsEA.inspectors.phpUnit;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocRef;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.PhpDocCommentImpl;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.kalessil.phpStorm.phpInspectionsEA.openApi.BasePhpElementVisitor;
import com.kalessil.phpStorm.phpInspectionsEA.openApi.BasePhpInspection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


public class PhpUnitTestsInspector extends BasePhpInspection {

    @NotNull
    public String getShortName() {
        return "PhpUnitTestsInspection";
    }

    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new BasePhpElementVisitor() {
            public void visitPhpMethod(Method method) {
                PhpClass clazz = method.getContainingClass();
                if (null == clazz) {
                    return;
                }

                String strMethodName = method.getName();
                PsiElement objMethodName = method.getNameIdentifier();
                if (StringUtil.isEmpty(strMethodName) || null == objMethodName) {
                    return;
                }

                PhpPsiElement previous = method.getPrevPsiSibling();
                if (!(previous instanceof PhpDocCommentImpl)) {
                    return;
                }

                Collection<PhpDocTag> tags = PsiTreeUtil.findChildrenOfType(previous, PhpDocTag.class);
                for (PhpDocTag tag : tags) {
                    if (tag.getName().equals("@depends") && tag.getFirstPsiChild() instanceof PhpDocRef) {
                        PhpDocRef methodNeeded = (PhpDocRef) tag.getFirstPsiChild();
                        if (null == clazz.findMethodByName(methodNeeded.getText())) {
                            holder.registerProblem(objMethodName, "@depends referencing non-existing method", ProblemHighlightType.GENERIC_ERROR);
                        }
                    }
                }
                tags.clear();
            }
        };
    }
}
