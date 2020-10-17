//
//  HomeUI.swift
//  iosApp
//
//  Created by Coder on 10/14/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

struct HomeUI: Identifiable {
    let id: Int
    let home: Any
    
    init(pos: Int, home: Any) {
        self.id = pos
        self.home = home
    }
}
