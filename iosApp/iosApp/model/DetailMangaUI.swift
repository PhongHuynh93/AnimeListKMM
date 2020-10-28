//
//  DetailMangaUI.swift
//  iosApp
//
//  Created by Coder on 10/18/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation

struct DetailMangaUI: Identifiable {
    let id = UUID()
    let item: Any
    
    init(item: Any) {
        self.item = item
    }
}
