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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.javascript.api.tree.expression.CallExpressionTree;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "S2714",
  name = "Element type selectors should not be used with class selectors",
  priority = Priority.MAJOR,
  tags = {Tags.JQUERY, Tags.PERFORMANCE, Tags.USER_EXPERIENCE})
@SqaleConstantRemediation("2min")
public class ElementUsedWithClassSelectorCheck extends AbstractJQuerySelectorOptimizationCheck {

  private static final String MESSAGE = "Remove \"%s\" in this selector.";
  private static final Pattern elementUsedWithClassSelectorPattern = Pattern.compile("(\\w+)\\.([\\w_-]+)");

  @Override
  protected void visitSelector(String selector, CallExpressionTree tree) {
    Matcher matcher = elementUsedWithClassSelectorPattern.matcher(selector);
    // ignore 2 parameters to not consider such cases: $("div.className", someContext)
    if (tree.arguments().parameters().size() == 1 && matcher.matches()) {
      addIssue(tree, String.format(MESSAGE, matcher.group(1)));
    }
  }
}
