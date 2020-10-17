//
//  MangaView.swift
//  iosApp
//
//  Created by Coder on 10/13/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import KingfisherSwiftUI

struct MangaView: View {
    let mangaUI: MangaUI
    var body: some View {
        ZStack() {
            KFImage(URL(string: mangaUI.manga.imageUrl))
                .resizable()
                .aspectRatio(2/2.6, contentMode:.fit)
                .cornerRadius(12)
                .overlay(TitleMangaOverlay(title: mangaUI.manga.title))
        }
    }
}

private struct TitleMangaOverlay: View {
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
