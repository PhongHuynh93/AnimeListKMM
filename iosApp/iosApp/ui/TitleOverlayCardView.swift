//
//  TitleOverlayCardView.swift
//  iosApp
//
//  Created by Coder on 10/27/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI

struct TitleOverlayCardView: View {
    let title: String
    
    var gradient: LinearGradient {
        LinearGradient(gradient: Gradient(colors: [
            Color.black.opacity(0.9),
            Color.black.opacity(0)
        ]), startPoint: .bottom, endPoint: .center)
    }
    
    var body: some View {
        ZStack(alignment: .bottomLeading) {
            RoundedRectangle(cornerRadius: 12).fill(gradient)
            Text(title).bold().lineLimit(2).padding(8)
        }
        .foregroundColor(.white)
    }
}
