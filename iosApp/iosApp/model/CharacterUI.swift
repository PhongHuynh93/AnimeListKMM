//
//  CharacterUI.swift
//  iosApp
//
//  Created by Coder on 10/18/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

struct CharacterUI: Identifiable {
    let id = UUID()
    let item: Character
    
    init(item: Character) {
        self.item = item
    }
}
