package vexatos.factumopus.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author Vexatos
 */
@SideOnly(Side.CLIENT)
public class CompressorPumpModel extends ModelBase {
    public ModelRenderer Center;
    public ModelRenderer Closed;
    public ModelRenderer Main;
    public ModelRenderer Cube1;
    public ModelRenderer Cube2;
    public ModelRenderer Side1;
    public ModelRenderer Side2;
    public ModelRenderer Top1_1;
    public ModelRenderer Top1_2;
    public ModelRenderer Bottom1_1;
    public ModelRenderer Bottom1_2;
    public ModelRenderer Top2_1;
    public ModelRenderer Top2_2;
    public ModelRenderer Bottom2_1;
    public ModelRenderer Bottom2_2;

    public CompressorPumpModel() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.Main = new ModelRenderer(this, 0, 33);
        this.Main.setRotationPoint(2.0F, 0.0F, -3.0F);
        this.Main.addBox(0.0F, 0.0F, 0.0F, 12, 3, 12, 0.0F);
        this.Top2_1 = new ModelRenderer(this, 29, 14);
        this.Top2_1.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.Top2_1.addBox(0.0F, 0.0F, 0.0F, 1, 3, 4, 0.0F);
        this.Cube2 = new ModelRenderer(this, 29, 0);
        this.Cube2.setRotationPoint(5.0F, 0.0F, 2.0F);
        this.Cube2.addBox(0.0F, 0.0F, 0.0F, 2, 3, 3, 0.0F);
        this.Bottom2_2 = new ModelRenderer(this, 29, 22);
        this.Bottom2_2.setRotationPoint(11.0F, 0.0F, 2.0F);
        this.Bottom2_2.addBox(0.0F, 0.0F, 0.0F, 1, 3, 4, 0.0F);
        this.Closed = new ModelRenderer(this, 0, 23);
        this.Closed.setRotationPoint(4.0F, 1.0F, -1.0F);
        this.Closed.addBox(0.0F, 0.0F, 0.0F, 8, 1, 8, 0.0F);
        this.Top1_1 = new ModelRenderer(this, 29, 7);
        this.Top1_1.setRotationPoint(1.0F, 0.0F, -3.0F);
        this.Top1_1.addBox(0.0F, 0.0F, 0.0F, 1, 3, 3, 0.0F);
        this.Bottom1_1 = new ModelRenderer(this, 38, 7);
        this.Bottom1_1.setRotationPoint(10.0F, 0.0F, -3.0F);
        this.Bottom1_1.addBox(0.0F, 0.0F, 0.0F, 1, 3, 3, 0.0F);
        this.Bottom2_1 = new ModelRenderer(this, 29, 22);
        this.Bottom2_1.setRotationPoint(11.0F, 0.0F, -4.0F);
        this.Bottom2_1.addBox(0.0F, 0.0F, 0.0F, 1, 3, 4, 0.0F);
        this.Cube1 = new ModelRenderer(this, 29, 0);
        this.Cube1.setRotationPoint(5.0F, 0.0F, -3.0F);
        this.Cube1.addBox(0.0F, 0.0F, 0.0F, 2, 3, 3, 0.0F);
        this.Side1 = new ModelRenderer(this, 0, 6);
        this.Side1.setRotationPoint(1.0F, 0.0F, -5.0F);
        this.Side1.addBox(0.0F, 0.0F, 0.0F, 10, 3, 2, 0.0F);
        this.Side2 = new ModelRenderer(this, 0, 12);
        this.Side2.setRotationPoint(1.0F, 0.0F, 5.0F);
        this.Side2.addBox(0.0F, 0.0F, 0.0F, 10, 3, 2, 0.0F);
        this.Top1_2 = new ModelRenderer(this, 29, 7);
        this.Top1_2.setRotationPoint(1.0F, 0.0F, 2.0F);
        this.Top1_2.addBox(0.0F, 0.0F, 0.0F, 1, 3, 3, 0.0F);
        this.Bottom1_2 = new ModelRenderer(this, 38, 7);
        this.Bottom1_2.setRotationPoint(10.0F, 0.0F, 2.0F);
        this.Bottom1_2.addBox(0.0F, 0.0F, 0.0F, 1, 3, 3, 0.0F);
        this.Center = new ModelRenderer(this, 0, 0);
        this.Center.setRotationPoint(2.0F, 0.0F, 2.0F);
        this.Center.addBox(0.0F, 0.0F, 0.0F, 12, 3, 2, 0.0F);
        this.Top2_2 = new ModelRenderer(this, 29, 14);
        this.Top2_2.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.Top2_2.addBox(0.0F, 0.0F, 0.0F, 1, 3, 4, 0.0F);
        this.Center.addChild(this.Top2_1);
        this.Center.addChild(this.Cube2);
        this.Center.addChild(this.Bottom2_2);
        this.Center.addChild(this.Top1_1);
        this.Center.addChild(this.Bottom1_1);
        this.Center.addChild(this.Bottom2_1);
        this.Center.addChild(this.Cube1);
        this.Center.addChild(this.Side1);
        this.Center.addChild(this.Side2);
        this.Center.addChild(this.Top1_2);
        this.Center.addChild(this.Bottom1_2);
        this.Center.addChild(this.Top2_2);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Main.render(f5);
        this.Closed.render(f5);
        this.Center.render(f5);
    }
}
