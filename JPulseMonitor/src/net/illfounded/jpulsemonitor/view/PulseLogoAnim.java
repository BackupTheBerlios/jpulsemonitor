/*
 * This file is part of the JPulsemonitor.
 *
 * JPulsemonitor is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package net.illfounded.jpulsemonitor.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import com.jgoodies.animation.Animation;
import com.jgoodies.animation.Animations;
import com.jgoodies.animation.animations.BasicTextAnimation;
import com.jgoodies.animation.components.BasicTextLabel;

/**
 * @author Adrian Buerki <ad@illfounded.net>
 * 
 * Renders an intro animation using the JGoodies framework.
 */
public class PulseLogoAnim {
    private BasicTextLabel label1;
    private Animation animation;

    /**
     * Returns the animation.
     * 
     * @return the animation
     */
    public Animation animation() {
        return animation;
    }
    
    /**
     * Creates and configures the UI components.
     */
    private void initComponents() {
        Font font = new Font("Tahoma", Font.BOLD, 18);
        label1 = new BasicTextLabel(" ");
        label1.setFont(font);
        label1.setBounds(0, 0, 350, 100);
        label1.setOpaque(false);
    }
    
    /**
     * Builds this panel with.
     * 
     * @return the panel.
     */
    public JPanel build() {
        initComponents();
        animation = createAnimation();
        
        JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, 350, 100);
        panel.add(label1);

        return panel;
    }

    /**
     * Creates and answers a composed animation.
     * 
     * @return the composed animation
     */
    private Animation createAnimation() {
        Animation welcome =
            BasicTextAnimation.defaultFade(
                label1,
                2700,
                "Welcome to",
                Color.darkGray);

        Animation jPulseAnimation =
            BasicTextAnimation.defaultFade(
                label1,
                2700,
                "the JPulseMonitor",
                Color.darkGray);

        Animation byAnimation =
            BasicTextAnimation.defaultFade(
                label1,
                2700,
                "by Adrian Buerki",
                Color.darkGray);

        Animation all =
            Animations.sequential(
                new Animation[] {
                		welcome,
						Animations.pause(1000),
						jPulseAnimation,
						Animations.pause(1000),
						byAnimation,
						Animations.pause(1000)
                    });

        return all;
    }

}