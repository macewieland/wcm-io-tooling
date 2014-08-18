/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.testing.mock.sling;

import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.adapter.AdapterManager;

/**
 * Wrapper for {@link MockAdapterManager} which makes sure multiple unit tests running in parallel do not
 * get in conflict with each other. Instead, a different {@link MockAdapterManager} is used per thread.
 */
class ThreadsafeMockAdapterManagerWrapper implements AdapterManager {

  private static final ThreadLocal<MockAdapterManager> THREAD_LOCAL = new ThreadLocal<MockAdapterManager>() {
    @Override
    protected MockAdapterManager initialValue() {
      return new MockAdapterManager();
    }
  };

  @Override
  public <AdapterType> AdapterType getAdapter(final Object adaptable, final Class<AdapterType> type) {
    MockAdapterManager adapterManager = THREAD_LOCAL.get();
    return adapterManager.getAdapter(adaptable, type);
  }

  /**
   * Register a adapter factory
   * @param adapterFactory Adapter factory
   */
  public void register(final AdapterFactory adapterFactory) {
    MockAdapterManager adapterManager = THREAD_LOCAL.get();
    adapterManager.register(adapterFactory);
  }

  /**
   * Removes all registrations from adapter factory.
   */
  public void clearRegistrations() {
    MockAdapterManager adapterManager = THREAD_LOCAL.get();
    adapterManager.clearRegistrations();
  }

}