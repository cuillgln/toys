
package io.cuillgln.toys.infrastructure.metrics;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.dropwizard.DropwizardConfig;
import io.micrometer.core.instrument.dropwizard.DropwizardMeterRegistry;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

public class MetricsConfiguration {

	public MetricRegistry dropwizardRegistry() {
		return new MetricRegistry();
	}

	public ConsoleReporter consoleReporter() {
		ConsoleReporter reporter = ConsoleReporter.forRegistry(dropwizardRegistry())
						.convertRatesTo(TimeUnit.SECONDS)
						.convertDurationsTo(TimeUnit.MILLISECONDS)
						.build();
		reporter.start(1, TimeUnit.SECONDS);
		return reporter;
	}

	public MeterRegistry registry() {
		DropwizardConfig dropwizardConfig = new DropwizardConfig() {

			@Override
			public String prefix() {
				return "console";
			}

			@Override
			public String get(String key) {
				return null;
			}
		};

		return new DropwizardMeterRegistry(dropwizardConfig, dropwizardRegistry(),
						HierarchicalNameMapper.DEFAULT, Clock.SYSTEM) {

			@Override
			protected Double nullGaugeValue() {
				return 0.0d;
			}
		};
	}
}
