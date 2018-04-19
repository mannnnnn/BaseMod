package basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.BaseMod;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard", method="upgradeName")
public class OnCardUpgradedHook {

	public static void Prefix(Object __obj_instance) {
		BaseMod.publishOnCardUpgraded((AbstractCard) __obj_instance);
	}
	
}
