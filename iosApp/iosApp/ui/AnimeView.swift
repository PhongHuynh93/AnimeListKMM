//
//  AnimeView.swift
//  iosApp
//
//  Created by Coder on 10/27/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import KingfisherSwiftUI

struct AnimeView: View {
    let item: AnimeUI
    var body: some View {
        ZStack() {
            KFImage(URL(string: item.item.imageUrl))
                .placeholder {
                    VStack {
                        Color.gray
                    }
                }
                .resizable()
                .aspectRatio(mangaRatio, contentMode:.fit)
                .cornerRadius(radiusImage)
                .overlay(TitleOverlayCardView(title: item.item.title))
        }
    }
}
