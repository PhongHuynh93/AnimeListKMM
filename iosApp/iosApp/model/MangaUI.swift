//
//  MangaUI.swift
//  iosApp
//
//  Created by Coder on 10/17/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

struct MangaUI: Identifiable {
    let id: Int
    let manga: Manga
    
    init(pos: Int, manga: Manga) {
        self.id = pos
        self.manga = manga
    }
}
