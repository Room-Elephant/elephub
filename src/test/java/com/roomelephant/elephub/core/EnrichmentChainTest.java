package com.roomelephant.elephub.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import com.roomelephant.elephub.core.enricher.Enricher;
import com.roomelephant.elephub.core.enricher.EnrichmentChain;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnrichmentChainTest {

  private EnrichmentChain<ExternalStub, InternalStub> victim;

  @BeforeEach
  void setUp() {
    victim = new EnrichmentChain<>();
  }

  @Test
  void shouldApplyAllEnrichers() {
    String name = "external-name";
    String address = "external-address";
    ExternalStub external = new ExternalStub(name, address);
    victim.addEnricher((e, i) -> i.setName(e.name()))
        .addEnricher((e, i) -> i.setAddress(e.address()));

    InternalStub internal = victim.enrich(external, InternalStub::new);

    assertEquals(name, internal.getName());
    assertEquals(address, internal.getAddress());
  }

  @Test
  void shouldApplyAllEnrichersInOrder() {
    ExternalStub external = new ExternalStub();
    Enricher<ExternalStub, InternalStub> enricher1 = mock(Enricher.class);
    Enricher<ExternalStub, InternalStub> enricher2 = mock(Enricher.class);
    victim.addEnricher(enricher1)
        .addEnricher(enricher2);

    InternalStub internal = victim.enrich(external, InternalStub::new);

    InOrder inOrder = inOrder(enricher1, enricher2);
    inOrder.verify(enricher1).enrich(external, internal);
    inOrder.verify(enricher2).enrich(external, internal);
    assertNotNull(internal);
  }

  @Test
  void shouldReturnNewInternalObjectEachTime() {
    ExternalStub external = new ExternalStub("name1", "address1");
    victim.addEnricher((e, i) -> i.setName(e.name()));

    InternalStub first = victim.enrich(external, InternalStub::new);
    InternalStub second = victim.enrich(external, InternalStub::new);

    assertNotEquals(first, second);
  }

  @Test
  void shouldWorkWithNoEnrichers() {
    ExternalStub external = new ExternalStub("irrelevant", "irrelevant");

    InternalStub internal = victim.enrich(external, InternalStub::new);

    assertNotNull(internal);
    assertNull(internal.getName());
    assertNull(internal.getAddress());
  }

  @Test
  void shouldAllowChaining() {
    EnrichmentChain<ExternalStub, InternalStub> returned = victim.addEnricher((e, i) -> {
    });

    assertEquals(returned, victim);
  }

  record ExternalStub(String name, String address) {
    public ExternalStub() {
      this(null, null);
    }
  }

  @Getter
  @Setter
  static class InternalStub {
    private String name;
    private String address;
  }
}