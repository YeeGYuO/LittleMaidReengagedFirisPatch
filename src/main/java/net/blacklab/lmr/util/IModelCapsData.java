package net.blacklab.lmr.util;

import net.blacklab.lmr.entity.maidmodel.base.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.caps.IModelCaps;

/**
 * マルチモデルのパラメータを管理する
 * @author firis-games
 *
 */
public interface IModelCapsData extends IModelCaps {

	/**
	 * ModelMultiBaseへ初期値を設定する
	 * @param model
	 * @param modelCaps
	 */
	public void setModelMultiFromModelCaps(ModelMultiBase model, float entityYaw, float partialTicks);
	
}
