package com.roomelephant.elephub.adapters.docker.fetch.mapper.enricher;

import static org.mockito.Mockito.verify;

import com.roomelephant.elephub.container.Container;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InnerEnricherTest {

  @Mock
  private com.github.dockerjava.api.model.Container externalContainer;

  @Mock
  private Container.ContainerBuilder builder;

  private final InnerEnricher victim = new InnerEnricher();

  @Test
  void shouldEnrichBuilderWithInnerInformation() {
    victim.enrich(externalContainer, builder);

    verify(builder).inner(externalContainer);
  }
}