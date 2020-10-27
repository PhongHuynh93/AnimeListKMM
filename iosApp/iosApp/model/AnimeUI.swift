//
//  AnimeUI.swift
//  iosApp
//
//  Created by Coder on 10/27/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

struct AnimeUI: Identifiable {
    let id = UUID()
    let item: Anime
    
    init(item: Anime) {
        self.item = item
    }
}
