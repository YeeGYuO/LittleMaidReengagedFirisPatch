package net.firis.lmt.client.model;

import net.blacklab.lmr.entity.maidmodel.ModelLittleMaidBase;
import net.blacklab.lmr.entity.maidmodel.ModelMultiBase;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.firis.lmt.common.modelcaps.PlayerModelCaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * マルチモデルを扱うためのラッパーモデル
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public class ModelLittleMaidMultiModel extends ModelBase {

	private PlayerModelCaps playerCaps = null;
	private ModelMultiBase playerModel = null;
	
	/**
	 * コンストラクタ
	 */
	public ModelLittleMaidMultiModel() {
	}
	
	/**
	 * アーマーモデルのセットアップ
	 * 
	 * doRenderのタイミングで必要な情報を内部変数へセットする
	 * ※重複処理を排除するため
	 */
	public void initPlayerModel(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		initPlayerModel((EntityPlayer) entity);
		
	}
	
	/**
	 * モデルを利用するための設定を行う
	 * @param player
	 */
	public void initPlayerModel(EntityPlayer player) {
		
		//プレイヤーモデルの準備
		playerModel = PlayerModelManager.getPlayerModel(player);
		playerCaps = getModelCaps(playerModel, player);
		
		playerModel.showAllParts();
		
	}
	
	/**
	 * メイドさんの向きなどを設定
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		
		//setRotationAngles		
		playerModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, playerCaps);
		
	}
	
	/**
	 * メイドさんのアニメーション設定
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		
		//setLivingAnimations
		playerModel.setLivingAnimations(playerCaps, limbSwing, limbSwingAmount, partialTickTime);
	}
	
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		//描画する
		playerModel.render(playerCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
	}
	
	/**
	 * マルチモデル描画用ModelCapsをセットアップ
	 * 
	 * マルチモデル側のsetCapsValueを呼び出して最低限必要な情報を設定する
	 * 内部変数にセットする
	 * @param player
	 * @return
	 */
	protected PlayerModelCaps getModelCaps(ModelMultiBase modelMain, EntityPlayer player) {
		
		PlayerModelCaps caps = new PlayerModelCaps(player);
		
		//モデルにCaps情報を設定する
		caps.setModelMultiBaseCapsFromModelCaps(modelMain);
		
		return caps;
	}
	
	public void armsPostRenderer(EnumHandSide handSide, float scale) {
		
		if (EnumHandSide.RIGHT == handSide) {
    		this.playerModel.Arms[0].postRender(scale);
     	} else if(EnumHandSide.LEFT == handSide) {
    		this.playerModel.Arms[1].postRender(scale);   		
    	}
	}
	
	/**
	 * 一人称視点の手を描画する
	 * @param player
	 * @param handSide
	 */
	public void renderFirstPersonArm(EntityPlayer player) {
		
		//プレイヤーモデルの準備
		this.initPlayerModel(player);
		
		//テクスチャバインド
		Minecraft.getMinecraft().getTextureManager().bindTexture(PlayerModelManager.getPlayerTexture(player));
		
		//お手ての位置調整
		GlStateManager.translate(0.0F, 0.25F, 0.0F);

		//お手てを描画
		playerModel.renderFirstPersonHand(playerCaps);
	}
	
	/**
	 * マルチモデルのpostRenderを呼び出す
	 * 
	 * 描画位置の調整
	 */
	public void modelPostRender(EnumMultiModelPartsType modelType, EntityPlayer player, float scale) {
		
		//プレイヤーモデルの準備
		this.initPlayerModel(player);
		
		
		//メイドベースの場合のみ調整する
		if (!(this.playerModel instanceof ModelLittleMaidBase)) return;
		
		ModelLittleMaidBase maidModel = (ModelLittleMaidBase) this.playerModel;
		
		//位置調整
		switch (modelType) {
		case HEAD:
			if (maidModel.bipedHead == null) return;
			maidModel.bipedHead.postRender(scale);
			break;
		case BODY:
			if (maidModel.bipedBody == null) return;
			maidModel.bipedBody.postRender(scale);
			break;
		}
	}
	
	/**
	 * postRender用のパーツ定義
	 * @author firis-games
	 *
	 */
	public enum EnumMultiModelPartsType {
		HEAD,
		BODY;
	}
	
}