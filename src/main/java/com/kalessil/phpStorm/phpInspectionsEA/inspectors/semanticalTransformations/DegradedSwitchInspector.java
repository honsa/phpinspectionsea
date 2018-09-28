package com.kalessil.phpStorm.phpInspectionsEA.inspectors.semanticalTransformations;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.psi.elements.PhpCase;
import com.jetbrains.php.lang.psi.elements.PhpSwitch;
import com.kalessil.phpStorm.phpInspectionsEA.openApi.BasePhpElementVisitor;
import com.kalessil.phpStorm.phpInspectionsEA.openApi.BasePhpInspection;
import org.jetbrains.annotations.NotNull;

/*
 * This file is part of the Php Inspections (EA Extended) package.
 *
 * (c) Vladimir Reznichenko <kalessil@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

public class DegradedSwitchInspector extends BasePhpInspection {
    private static final String messageDegraded    = "Switch construct behaves as if-else, consider refactoring.";
    private static final String messageOnlyDefault = "Switch construct has default case only, consider leaving only the default case's body.";

    @NotNull
    public String getShortName() {
        return "DegradedSwitchInspection";
    }

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new BasePhpElementVisitor() {
            @Override
            public void visitPhpSwitch(@NotNull PhpSwitch expression) {
                if (this.isContainingFileSkipped(expression)) { return; }

                if (expression.getDefaultCase() != null) {
                    final PhpCase[] cases = expression.getCases();
                    if (cases.length == 0) {
                        holder.registerProblem(expression.getFirstChild(), messageOnlyDefault);
                    } else if (cases.length == 1) {
                        holder.registerProblem(expression.getFirstChild(), messageDegraded);
                    }
                }
            }
       };
    }
}