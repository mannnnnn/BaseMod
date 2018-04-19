package basemod.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCardUpgradedSubscriber extends ISubscriber {
	void receiveCardUpgraded(AbstractCard c);
}
