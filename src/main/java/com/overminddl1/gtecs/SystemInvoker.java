package com.overminddl1.gtecs;

import com.artemis.BaseSystem;
import com.artemis.SystemInvocationStrategy;
import com.artemis.utils.Bag;
import com.overminddl1.gtecs.systems.states.IInit;
import com.overminddl1.gtecs.systems.states.IState;
import com.overminddl1.gtecs.systems.states.ITick;

public class SystemInvoker extends SystemInvocationStrategy {
	static final int STATE_ALL = 0;
	static final int STATE_NEVER = 1;
	static final int STATE_INIT = 2;
	static final int STATE_TICK = 3;

	static final int COUNT_STATES = 4;

	public int state = SystemInvoker.STATE_ALL;

	protected Bag<BaseSystem>[] stateSystems;

	@Override
	protected void initialize() {
		super.initialize();

		stateSystems = new Bag[SystemInvoker.COUNT_STATES];
		stateSystems[0] = systems;
		for (int i = 1; i < SystemInvoker.COUNT_STATES; ++i) {
			stateSystems[i] = new Bag<BaseSystem>(BaseSystem.class);
		}

		final BaseSystem[] systemsData = systems.getData();
		for (int i = 1, s = systems.size(); s > i; i++) {
			final BaseSystem system = systemsData[i];
			if (system instanceof IInit || !(system instanceof IState)) {
				stateSystems[SystemInvoker.STATE_INIT].add(system);
			}
			if (system instanceof ITick || !(system instanceof IState)) {
				stateSystems[SystemInvoker.STATE_TICK].add(system);
			}
		}
	}

	/**
	 * Processes all systems in order.
	 *
	 * Need to guarantee the ECS is in a sane state using calls to
	 * #updateEntityStates before each call to a system, and after the last system
	 * has been called, or if no systems have been called at all.
	 */
	@Override
	protected void process() {
		if (state == SystemInvoker.STATE_NEVER) {
			throw new RuntimeException("Do not process with STATE_NEVER!");
		}

		final Bag<BaseSystem> systemBag = stateSystems[state];
		final BaseSystem[] systemsData = systemBag.getData();
		for (int i = 0, s = systemBag.size(); s > i; i++) {
			if (disabled.get(i)) {
				continue;
			}

			updateEntityStates();
			systemsData[i].process();
		}

		updateEntityStates();
	}
}
