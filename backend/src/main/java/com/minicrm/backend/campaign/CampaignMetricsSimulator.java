package com.minicrm.backend.campaign;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import org.springframework.stereotype.Component;

// 실제 발송 없이 대상 고객 수를 기준으로 채널별로 현실적인 범위의 성과를 생성한다.
// - 발송: 대상 수 x 도달률
// - 오픈: 발송 x 오픈율, 클릭: 오픈 x 클릭률, 전환: 클릭 x 전환율
// - 매출: 전환 x 객단가(30,000 ~ 120,000원)
// 운영에서는 스레드별 ThreadLocalRandom을 사용하고, 테스트에서는 시드가 고정된 RandomGenerator를 주입해 결과를 고정할 수 있다.
@Component
public class CampaignMetricsSimulator {

	private final Supplier<RandomGenerator> randomSupplier;

	public CampaignMetricsSimulator() {
		this(ThreadLocalRandom::current);
	}

	CampaignMetricsSimulator(Supplier<RandomGenerator> randomSupplier) {
		this.randomSupplier = randomSupplier;
	}

	public SimulatedMetrics simulate(CampaignChannel channel, long targetCount) {
		RandomGenerator random = randomSupplier.get();
		ChannelRates rates = ChannelRates.of(channel);
		int sent = apply(random, targetCount, rates.minDelivery(), rates.maxDelivery());
		int open = apply(random, sent, rates.minOpen(), rates.maxOpen());
		int click = apply(random, open, rates.minClick(), rates.maxClick());
		int conversion = apply(random, click, rates.minConversion(), rates.maxConversion());

		BigDecimal revenue = BigDecimal.ZERO;
		for (int i = 0; i < conversion; i++) {
			revenue = revenue.add(BigDecimal.valueOf(random.nextLong(30_000, 120_001)));
		}
		return new SimulatedMetrics(sent, open, click, conversion, revenue.setScale(2, RoundingMode.HALF_UP));
	}

	private static int apply(RandomGenerator random, long base, double min, double max) {
		return (int) Math.round(base * random.nextDouble(min, max));
	}

	public record SimulatedMetrics(int sentCount, int openCount, int clickCount, int conversionCount,
			BigDecimal revenue) {
	}

	private record ChannelRates(double minDelivery, double maxDelivery, double minOpen, double maxOpen,
			double minClick, double maxClick, double minConversion, double maxConversion) {

		static ChannelRates of(CampaignChannel channel) {
			return switch (channel) {
				case EMAIL -> new ChannelRates(0.95, 0.99, 0.18, 0.35, 0.10, 0.25, 0.05, 0.15);
				case SMS -> new ChannelRates(0.97, 0.995, 0.80, 0.95, 0.05, 0.12, 0.05, 0.12);
				case OTHER -> new ChannelRates(0.90, 0.98, 0.30, 0.50, 0.05, 0.15, 0.03, 0.10);
			};
		}

	}

}
