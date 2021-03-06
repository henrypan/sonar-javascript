/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.javascript.checks;

import java.util.ArrayList;
import java.util.List;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.javascript.api.symbols.Symbol;
import org.sonar.plugins.javascript.api.symbols.Symbol.Kind;
import org.sonar.plugins.javascript.api.symbols.Usage;
import org.sonar.plugins.javascript.api.tree.ScriptTree;
import org.sonar.plugins.javascript.api.visitors.DoubleDispatchVisitorCheck;
import org.sonar.plugins.javascript.api.visitors.PreciseIssue;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "S3500",
  name = "Attempts should not be made to update \"const\" variables",
  priority = Priority.BLOCKER,
  tags = {Tags.BUG, Tags.ES2015})
@ActivatedByDefault
@SqaleConstantRemediation("15min")
public class UpdatedConstVariableCheck extends DoubleDispatchVisitorCheck {

  private static final String MESSAGE = "Correct this attempt to modify \"%s\" or use \"let\" in its declaration.";

  @Override
  public void visitScript(ScriptTree tree) {
    for (Symbol constSymbol : getContext().getSymbolModel().getSymbols(Kind.CONST_VARIABLE)) {

      Usage declaration = null;
      List<Usage> writeUsages = new ArrayList<>();

      for (Usage usage : constSymbol.usages()) {
        if (declaration == null && usage.isDeclaration() && usage.isWrite()) {
          declaration = usage;
        } else if (usage.isWrite()) {
          writeUsages.add(usage);
        }
      }

      if (declaration != null) {
        for (Usage writeUsage : writeUsages) {
          PreciseIssue preciseIssue = addIssue(writeUsage.identifierTree(), String.format(MESSAGE, constSymbol.name()));
          preciseIssue.secondary(declaration.identifierTree(), "Const declaration");
        }
      }

    }
  }
}
