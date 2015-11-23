/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011 SonarSource and Eriks Nukis
 * sonarqube@googlegroups.com
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.javascript.api.visitors;

import org.sonar.javascript.tree.impl.JavaScriptTree;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.lexical.SyntaxToken;

public class IssueLocation {

  private final Tree tree;
  private final String message;

  public IssueLocation(Tree tree, String message) {
    this.tree = tree;
    this.message = message;
  }

  public Tree tree() {
    return tree;
  }

  public String message() {
    return message;
  }

  public int startLine() {
    return firstToken().line();
  }

  public int startLineOffset() {
    return firstToken().column();
  }

  public int endLine() {
    return lastToken().line();
  }

  public int endLineOffset() {
    return lastToken().column() + lastToken().text().length();
  }

  private SyntaxToken firstToken() {
    return ((JavaScriptTree) tree).getFirstToken();
  }

  private SyntaxToken lastToken() {
    return ((JavaScriptTree) tree).getLastToken();
  }

}
