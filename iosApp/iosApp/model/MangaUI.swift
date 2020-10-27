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
    let id = UUID()
    let manga: Manga
    
    init(manga: Manga) {
        self.manga = manga
    }
}
