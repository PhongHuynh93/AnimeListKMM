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
    let manga: Manga
    var body: some View {
        ZStack() {
            KFImage(URL(string: manga.imageUrl))
                .resizable()
                .aspectRatio(2/2.6, contentMode: /*@START_MENU_TOKEN@*/.fill/*@END_MENU_TOKEN@*/)
            Text(manga.title)
        }
    }
}
