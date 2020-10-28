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
                .placeholder {
                    VStack {
                        Color.gray
                    }
                }
                .resizable()
                .aspectRatio(mangaRatio, contentMode:.fit)
                .cornerRadius(radiusImage)
                .overlay(TitleOverlayCardView(title: mangaUI.manga.title))
        }
    }
}
