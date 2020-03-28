package net.firis.lmt.client.renderer;

import org.lwjgl.opengl.GL11;

import net.firis.lmt.client.model.ModelLittleMaidMultiModel;
import net.firis.lmt.client.renderer.layer.LayerArmorLittleMaidMultiModel;
import net.firis.lmt.client.renderer.layer.LayerArrowLittleMaid;
import net.firis.lmt.client.renderer.layer.LayerCustomHeadLittleMaid;
import net.firis.lmt.client.renderer.layer.LayerElytraLittleMaid;
import net.firis.lmt.client.renderer.layer.LayerEntityOnShoulderLittleMaid;
import net.firis.lmt.client.renderer.layer.LayerHeldItemLittleMaidMultiModel;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * マルチモデルでプレイヤーモデル描画
 * @author firis-games
 *
 */
public class RendererMaidPlayerMultiModel extends RenderPlayer {
	
	protected static ModelBase dummyMainModel = new ModelPlayer(0.0F, false);
	
	/**
	 * コンストラクタ
	 * @param renderManagerIn
	 * @param modelBaseIn
	 * @param shadowSizeIn
	 */
	public RendererMaidPlayerMultiModel(RenderPlayer renderPlayer) {
		
		super(renderPlayer.getRenderManager());
		
		//初期化
		this.layerRenderers.clear();
		this.mainModel = new ModelLittleMaidMultiModel();
		this.shadowSize = 0.5F;
		
		//layer追加
		this.addLayer(new LayerHeldItemLittleMaidMultiModel(this));
		this.addLayer(new LayerArmorLittleMaidMultiModel(this));
		
		//Player用のlayerを一部改造
        this.addLayer(new LayerElytraLittleMaid(this));
        this.addLayer(new LayerCustomHeadLittleMaid(this));
        this.addLayer(new LayerEntityOnShoulderLittleMaid(renderManager, this));
        this.addLayer(new LayerArrowLittleMaid(this));
        
        
	}

	/**
	 * バインド用のテクスチャ
	 */
	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
		//EntityPlayerからテクスチャを取得する
		//メイドモデルの実装だとこの部分はnullを返却する
		return PlayerModelManager.getPlayerTexture(entity);
	}
	
	/**
	 * 試作段階のため通常のPlayerModelをダミーモデルとして返却する
	 */
	@Override
	public ModelPlayer getMainModel()
    {
        return (ModelPlayer) dummyMainModel;
    }
	
	/**
	 * model情報を取得する
	 * @return
	 */
	public ModelLittleMaidMultiModel getLittleMaidMultiModel() {
		return (ModelLittleMaidMultiModel) this.mainModel;
	}
	
	/**
	 * プレイヤーモデルの初期化をしてから描画処理を行う
	 */
	@Override
	public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
	
		//パラメータを初期化
		((ModelLittleMaidMultiModel) this.mainModel).initPlayerModel(entity, x, y, z, entityYaw, partialTicks);
		
		//法線の再計算
		//GlStateManager.enableRescaleNormal();
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		//描画処理
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
	}
	
	@Override
	public void renderRightArm(AbstractClientPlayer clientPlayer) {
		this.renderFirstPersonArm(clientPlayer);
	}
	
	@Override
	public void renderLeftArm(AbstractClientPlayer clientPlayer) {
		this.renderFirstPersonArm(clientPlayer);		
	}
	
	/**
	 * 一人称視点の手を描画する
	 */
	protected void renderFirstPersonArm(AbstractClientPlayer clientPlayer) {
		
		((ModelLittleMaidMultiModel) this.mainModel).renderFirstPersonArm(clientPlayer);
		
	}
	
}