/*
 * Copyright 2016-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.features.go;

import com.facebook.buck.cxx.toolchain.CxxPlatform;
import com.facebook.buck.model.Flavor;
import com.facebook.buck.model.FlavorConvertible;
import com.facebook.buck.rules.AddToRuleKey;
import com.facebook.buck.rules.AddsToRuleKey;
import com.facebook.buck.rules.Tool;
import com.facebook.buck.util.immutables.BuckStyleImmutable;
import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import org.immutables.value.Value;

/** Abstracting the tooling/flags/libraries used to build Go rules. */
@Value.Immutable(copy = true)
@BuckStyleImmutable
abstract class AbstractGoPlatform implements FlavorConvertible, AddsToRuleKey {

  @AddToRuleKey
  abstract String getGoOs();

  @AddToRuleKey
  abstract String getGoArch();

  // TODO: For now, we rely on Go platforms having the same "name" as the C/C++ platforms they wrap,
  // due to having to lookup the Go platform in the C/C++ interfaces that Go rules implement, into
  // which only C/C++ platform objects are threaded.
  @Override
  @Value.Default
  public Flavor getFlavor() {
    return getCxxPlatform().getFlavor();
  }

  public abstract Path getGoRoot();

  public abstract Path getToolDir();

  public abstract ImmutableList<Path> getAssemblerIncludeDirs();

  public abstract Tool getCompiler();

  public abstract Tool getAssembler();

  public abstract Tool getCGo();

  public abstract Tool getPacker();

  public abstract Tool getLinker();

  public abstract Tool getCover();

  public abstract CxxPlatform getCxxPlatform();
}
